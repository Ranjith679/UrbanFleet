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
import com.urbanfleet.events.delivery.DeliveryAssignedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class DeliveryService {

    private final RiderRepository riderRepository;

    private final DeliveryRepository deliveryRepository;

    private final DeliveryProducer producer;

    private final OrderClient orderClient;

    public DeliveryService(RiderRepository riderRepository, DeliveryRepository deliveryRepository, DeliveryProducer producer, OrderClient orderClient) {
        this.riderRepository = riderRepository;
        this.deliveryRepository = deliveryRepository;
        this.producer = producer;
        this.orderClient = orderClient;
    }

    public void assign(UUID orderId) {

        RestaurantLocationResponse restaurant = orderClient.getRestaurantLocation(orderId);

        System.out.println(restaurant.getLatitude());
        System.out.println(restaurant.getLongitude());

        log.info("Assigning delivery for order {}", orderId);


        List<Rider> riders = riderRepository.findByAvailableTrue();


        Rider nearest = null;

        double shortestDistance = Double.MAX_VALUE;

        for(Rider rider : riders){

            double distance =
                    DistanceCalculator.distance(
                            restaurant.getLatitude(),
                            restaurant.getLongitude(),
                            rider.getLatitude(),
                            rider.getLongitude());

            if(distance < shortestDistance){

                shortestDistance = distance;
                nearest = rider;

            }

        }

        if(nearest == null){
            throw new RuntimeException("No rider available");
        }

        nearest.setAvailable(false);

        riderRepository.save(nearest);

        // Create delivery record
        Delivery delivery = new Delivery();

        delivery.setOrderId(orderId);

        delivery.setRiderId(nearest.getId());

        delivery.setStatus(DeliveryStatus.ASSIGNED);

        deliveryRepository.save(delivery);

        log.info("Publishing DeliveryAssignedEvent for order {}", orderId);

        producer.send(new DeliveryAssignedEvent(orderId, nearest.getId()));
    }
}