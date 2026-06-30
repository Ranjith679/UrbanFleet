package com.urbanfleet.delivery_service.service;

import com.urbanfleet.delivery_service.constants.DeliveryStatus;
import com.urbanfleet.delivery_service.entity.Delivery;
import com.urbanfleet.delivery_service.entity.Rider;
import com.urbanfleet.delivery_service.repository.DeliveryRepository;
import com.urbanfleet.delivery_service.repository.RiderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DeliveryService {

    private final RiderRepository riderRepository;

    private final DeliveryRepository deliveryRepository;

    private final DeliveryProducer producer;

    public void assign(UUID orderId) {

        // Find one available rider
        Rider rider = riderRepository
                .findFirstByAvailableTrue()
                .orElseThrow(() ->
                        new RuntimeException("No rider available"));

        // Rider becomes busy
        rider.setAvailable(false);

        riderRepository.save(rider);

        // Create delivery record
        Delivery delivery = new Delivery();

        delivery.setOrderId(orderId);

        delivery.setRiderId(rider.getId());

        delivery.setStatus(DeliveryStatus.ASSIGNED);

        deliveryRepository.save(delivery);

        // Notify Order Service
        producer.send(
                new DeliveryAssignedEvent(
                        orderId,
                        rider.getId()
                )
        );
    }
}