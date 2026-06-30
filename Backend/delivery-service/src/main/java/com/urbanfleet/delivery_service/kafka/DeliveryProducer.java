package com.urbanfleet.delivery_service.kafka;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeliveryProducer {

    private final KafkaTemplate<String, DeliveryAssignedEvent> kafkaTemplate;

    public void send(DeliveryAssignedEvent event) {

        // Publish delivery assignment event
        kafkaTemplate.send("delivery-events", event);
    }
}
