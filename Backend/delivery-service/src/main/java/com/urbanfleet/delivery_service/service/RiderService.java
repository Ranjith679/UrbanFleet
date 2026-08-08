package com.urbanfleet.delivery_service.service;

import com.urbanfleet.delivery_service.constants.DeliveryStatus;
import com.urbanfleet.delivery_service.dto.RiderLocationRequest;
import com.urbanfleet.delivery_service.entity.Delivery;
import com.urbanfleet.delivery_service.entity.Rider;
import com.urbanfleet.delivery_service.kafka.DeliveryEventProducer;
import com.urbanfleet.delivery_service.repository.DeliveryRepository;
import com.urbanfleet.delivery_service.repository.RiderRepository;
import com.urbanfleet.events.delivery.RiderLocationUpdatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class RiderService {

    private final RiderRepository repository;
    private final DeliveryRepository deliveryRepository;
    private final DeliveryEventProducer producer;

    public Rider updateLocation(UUID riderId, RiderLocationRequest request) {

        Rider rider = repository.findById(riderId)
                .orElseThrow(() ->
                        new RuntimeException("Rider not found: " + riderId));

        // Update rider location
        rider.setLatitude(request.getLatitude());
        rider.setLongitude(request.getLongitude());
        rider.setOffline(false);
        rider.setLastLocationUpdate(LocalDateTime.now());

        Rider savedRider = repository.save(rider);

        log.info(
                "Updated location for rider {} -> {}, {}",
                riderId,
                request.getLatitude(),
                request.getLongitude()
        );

        // Find the rider's active delivery
        Delivery delivery = deliveryRepository
                .findByRiderIdAndStatusIn(
                        riderId,
                        List.of(
                                DeliveryStatus.ASSIGNED,
                                DeliveryStatus.ACCEPTED,
                                DeliveryStatus.PICKED_UP
                        )
                )
                .orElse(null);

        // Rider has no active delivery
        if (delivery == null) {

            log.info(
                    "Rider {} has no active delivery. Location event not published.",
                    riderId
            );

            return savedRider;
        }

        // Publish Kafka event
        producer.publishLocationUpdated(
                new RiderLocationUpdatedEvent(
                        delivery.getOrderId(),
                        riderId,
                        request.getLatitude(),
                        request.getLongitude()
                )
        );

        return savedRider;
    }
}