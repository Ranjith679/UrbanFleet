package com.urbanfleet.delivery_service.service;

import com.urbanfleet.delivery_service.clients.OrderClient;
import com.urbanfleet.delivery_service.constants.DeliveryStatus;
import com.urbanfleet.delivery_service.dto.RestaurantLocationResponse;
import com.urbanfleet.delivery_service.entity.Delivery;
import com.urbanfleet.delivery_service.entity.Rider;
import com.urbanfleet.delivery_service.kafka.DeliveryProducer;
import com.urbanfleet.delivery_service.repository.DeliveryRepository;
import com.urbanfleet.delivery_service.repository.RiderRepository;
import com.urbanfleet.delivery_service.utility.DistanceCalculator;
import com.urbanfleet.events.delivery.DeliveryAcceptedEvent;
import com.urbanfleet.events.delivery.DeliveryAssignedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class DeliveryService {

    private final RiderRepository riderRepository;

    private final RiderService riderService;

    private final DeliveryRepository deliveryRepository;

    private final DeliveryProducer producer;

    private final OrderClient orderClient;

    public DeliveryService(RiderRepository riderRepository, DeliveryRepository deliveryRepository, DeliveryProducer producer, OrderClient orderClient, RiderService riderService) {
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

        delivery.setRetryCount(0);

        deliveryRepository.save(delivery);

        log.info("Publishing DeliveryAssignedEvent for order {}", orderId);

        producer.sendAssigned(new DeliveryAssignedEvent(orderId, nearest.getId()));
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
        producer.sendAccepted(
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

    private void assignNextRider(Delivery delivery) {

        // Stop retrying after 3 attempts
        if (delivery.getRetryCount() >= 3) {

            delivery.setStatus(DeliveryStatus.CANCELLED);

            deliveryRepository.save(delivery);

            log.info("Delivery cancelled after 3 retries for order {}", delivery.getOrderId());

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

        deliveryRepository.save(delivery);

        log.info("Reassigned order {} to rider {} (Retry {})",
                delivery.getOrderId(),
                nearest.getId(),
                delivery.getRetryCount());

        // Notify other services
        producer.sendAssigned(
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

        producer.sendAssigned(
                new DeliveryAssignedEvent(
                        delivery.getOrderId(),
                        riderId
                )
        );
    }
}