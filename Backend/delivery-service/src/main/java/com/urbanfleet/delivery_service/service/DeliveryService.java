package com.urbanfleet.delivery_service.service;

import com.urbanfleet.delivery_service.clients.OrderClient;
import com.urbanfleet.delivery_service.constants.DeliveryStatus;
import com.urbanfleet.delivery_service.dto.CustomerLocationResponse;
import com.urbanfleet.delivery_service.dto.DeliveryTrackingResponse;
import com.urbanfleet.delivery_service.dto.RestaurantLocationResponse;
import com.urbanfleet.delivery_service.entity.Delivery;
import com.urbanfleet.delivery_service.entity.Rider;
import com.urbanfleet.delivery_service.kafka.DeliveryEventProducer;
import com.urbanfleet.delivery_service.repository.DeliveryRepository;
import com.urbanfleet.delivery_service.repository.RiderRepository;
import com.urbanfleet.delivery_service.utility.DistanceCalculator;
import com.urbanfleet.events.delivery.*;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class DeliveryService {

    private final RiderRepository riderRepository;

    private final RiderService riderService;

    private final DeliveryRepository deliveryRepository;

    private final DeliveryEventProducer producer;

    private final OrderClient orderClient;

    public DeliveryService(RiderRepository riderRepository, DeliveryRepository deliveryRepository, DeliveryEventProducer producer, OrderClient orderClient, RiderService riderService) {
        this.riderRepository = riderRepository;
        this.deliveryRepository = deliveryRepository;
        this.producer = producer;
        this.orderClient = orderClient;
        this.riderService = riderService;
    }

    public void assign(UUID orderId) {

        RestaurantLocationResponse restaurant = orderClient.getRestaurantLocation(orderId);

        log.info("Assigning delivery for order {}", orderId);

        List<Rider> riders = riderRepository.findByAvailableTrue();

        Rider nearest = findNearestRider(riders, restaurant);

        riderRepository.save(nearest);

        // Create delivery record
        Delivery delivery = new Delivery();

        delivery.setOrderId(orderId);

        delivery.setRiderId(nearest.getId());

        delivery.setStatus(DeliveryStatus.ASSIGNED);

        delivery.setAssignedAt(LocalDateTime.now());

        delivery.setRetryCount(0);

        deliveryRepository.save(delivery);

        log.info("Publishing DeliveryAssignedEvent for order {}", orderId);

        producer.publishAssigned(new DeliveryAssignedEvent(orderId, nearest.getId()));
    }


    public Rider findNearestRider(List<Rider> riders,
                                  RestaurantLocationResponse restaurant) {

        Rider nearest = null;
        double shortestDistance = Double.MAX_VALUE;

        for (Rider rider : riders) {

            double distance = DistanceCalculator.distance(
                    restaurant.getLatitude(),
                    restaurant.getLongitude(),
                    rider.getLatitude(),
                    rider.getLongitude());

            if (distance < shortestDistance) {
                shortestDistance = distance;
                nearest = rider;
            }
        }

        if (nearest == null) {
            throw new RuntimeException("No rider available");
        }

        return nearest;
    }

    public void accept(UUID deliveryId) {

        // Find delivery
        Delivery delivery = deliveryRepository.findById(deliveryId)
                .orElseThrow(() -> new RuntimeException("Delivery not found"));

        // Already accepted?
        if (delivery.getStatus() == DeliveryStatus.ACCEPTED) {
            throw new RuntimeException("Delivery already accepted");
        }

        // Don't allow accepting cancelled deliveries
        if (delivery.getStatus() == DeliveryStatus.CANCELLED) {
            throw new RuntimeException("Delivery is cancelled");
        }

        // Update status
        delivery.setStatus(DeliveryStatus.ACCEPTED);

        deliveryRepository.save(delivery);

        log.info("Delivery {} accepted by rider {}",
                delivery.getId(),
                delivery.getRiderId());

        // Publish Kafka event (we'll create this next)
        producer.publishAccepted(
                new DeliveryAcceptedEvent(
                        delivery.getOrderId(),
                        delivery.getRiderId()
                )
        );
    }

    public void reject(UUID deliveryId){

        Delivery delivery = deliveryRepository.findById(deliveryId).orElseThrow();

        Rider current = riderRepository.findById(delivery.getRiderId()).orElseThrow(()-> new RuntimeException("Rider not found"));

        current.setAvailable(true);

        riderRepository.save(current);

        assignNextRider(delivery);
    }

    public void assignNextRider(Delivery delivery) {

        // Stop retrying after 3 attempts
        if (delivery.getRetryCount() >= 3) {

            delivery.setStatus(DeliveryStatus.CANCELLED);

            deliveryRepository.save(delivery);

            log.info("Delivery cancelled after 3 retries for order {}", delivery.getOrderId());

            producer.publishCancelled(
                    new DeliveryCancelledEvent(
                            delivery.getId(),
                            delivery.getOrderId(),
                            delivery.getRiderId(),"All riders rejected/ did not accepted your delivery request"
                    )
            );

            return;
        }

        // Get restaurant location from Order Service
        RestaurantLocationResponse restaurant =
                orderClient.getRestaurantLocation(delivery.getOrderId());

        // Find all available riders
        List<Rider> riders = riderRepository.findByAvailableTrue();

        // Select nearest rider
        Rider nearest = findNearestRider(riders, restaurant);

        // Mark rider unavailable
        nearest.setAvailable(false);
        riderRepository.save(nearest);

        // Update existing delivery assignment
        delivery.setRiderId(nearest.getId());

        log.info("retry count = {}", delivery.getRetryCount());

        delivery.setRetryCount(delivery.getRetryCount() + 1);
        delivery.setStatus(DeliveryStatus.ASSIGNED);
        delivery.setAssignedAt(LocalDateTime.now());

        deliveryRepository.save(delivery);

        log.info("Reassigned order {} to rider {} (Retry {})",
                delivery.getOrderId(),
                nearest.getId(),
                delivery.getRetryCount());

        // Notify other services
        producer.publishAssigned(
                new DeliveryAssignedEvent(
                        delivery.getOrderId(),
                        nearest.getId()
                )
        );
    }

    public void reassignOfflineRider(UUID riderId) {

        Delivery delivery = deliveryRepository
                .findByRiderIdAndStatus(
                        riderId,
                        DeliveryStatus.ASSIGNED)
                .orElse(null);

        if (delivery == null) {

            log.info("No assigned delivery for offline rider {}", riderId);

            return;
        }

        log.info("Reassigning delivery {} because rider {} went offline",
                delivery.getId(),
                riderId);

        assignNextRider(delivery);

        producer.publishAssigned(
                new DeliveryAssignedEvent(
                        delivery.getOrderId(),
                        riderId
                )
        );
    }


    /***
     * PICKUP METHOD
     */

    public void pickup(UUID deliveryId){

        Delivery delivery =
                deliveryRepository.findById(deliveryId)
                        .orElseThrow();

        if(delivery.getStatus() != DeliveryStatus.ACCEPTED){

            throw new RuntimeException(
                    "Delivery must be ACCEPTED first."
            );
        }

        delivery.setStatus(DeliveryStatus.PICKED_UP);

        deliveryRepository.save(delivery);

        log.info(
                "Delivery {} picked up.",
                deliveryId
        );

        producer.publishPickedUp(new DeliveryPickedUpEvent(deliveryId, delivery.getOrderId(),delivery.getRiderId()));
    }


    /***
     * DELIVERY COMPLETE METHOD and me=akes the rider available for next irder
     */

    public void complete(UUID deliveryId){

        Delivery delivery =
                deliveryRepository.findById(deliveryId)
                        .orElseThrow();

        if(delivery.getStatus() != DeliveryStatus.PICKED_UP){

            throw new RuntimeException(
                    "Food has not been picked up."
            );
        }

        delivery.setStatus(DeliveryStatus.DELIVERED);

        Rider rider =
                riderRepository.findById(
                                delivery.getRiderId())
                        .orElseThrow();

        rider.setAvailable(true);

        riderRepository.save(rider);

        deliveryRepository.save(delivery);

        log.info(
                "Delivery {} completed.",
                deliveryId
        );

        producer.publishCompleted(
                new DeliveryCompletedEvent(
                        delivery.getId(),
                        delivery.getOrderId(),
                        delivery.getRiderId()
                )
        );
    }


    @Transactional
    public DeliveryTrackingResponse trackOrder(UUID orderId) {

        Delivery delivery = deliveryRepository.findByOrderId(orderId)
                .orElseThrow(() -> new RuntimeException("Delivery not found"));

        Rider rider = riderRepository.findById(delivery.getRiderId())
                .orElseThrow(() -> new RuntimeException("Rider not found"));

        CustomerLocationResponse customer =
                orderClient.getCustomerLocation(orderId);

        double remainingDistance =
                DistanceCalculator.distance(
                        rider.getLatitude(),
                        rider.getLongitude(),
                        customer.getLatitude(),
                        customer.getLongitude()
                );

        int eta =
                (int) Math.ceil((remainingDistance / 30.0) * 60);

        DeliveryTrackingResponse response =
                new DeliveryTrackingResponse();

        response.setOrderId(delivery.getOrderId());

        response.setRiderId(rider.getId());

        response.setRiderLatitude(rider.getLatitude());

        response.setRiderLongitude(rider.getLongitude());

        response.setCustomerLatitude(customer.getLatitude());

        response.setCustomerLongitude(customer.getLongitude());

        response.setRemainingDistanceKm(
                Math.round(remainingDistance * 100.0) / 100.0
        );

        response.setEstimatedArrivalMinutes(eta);

        response.setDeliveryStatus(delivery.getStatus());

        return response;
    }
}