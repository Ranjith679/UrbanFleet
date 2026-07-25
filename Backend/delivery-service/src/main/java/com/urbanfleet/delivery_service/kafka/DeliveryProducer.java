package com.urbanfleet.delivery_service.kafka;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class DeliveryProducer {

    private final KafkaTemplate<String, DeliveryAssignedEvent> kafkaTemplate;

    public DeliveryProducer(KafkaTemplate<String, DeliveryAssignedEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void send(DeliveryAssignedEvent event) {

        // Publish delivery assignment event
        kafkaTemplate.send("delivery-events", event);
    }
}
