package com.urbanfleet.payment_service.kafka;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentEventProducer {

    private final KafkaTemplate<String, PaymentEvent> kafkaTemplate;

    public void send(PaymentEvent event) {
        System.out.println("Kafka Sending: " + event);

        kafkaTemplate.send("payment-events", event);
    }
}
