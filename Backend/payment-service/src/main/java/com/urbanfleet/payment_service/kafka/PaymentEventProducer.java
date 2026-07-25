package com.urbanfleet.payment_service.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentEventProducer {

    private final KafkaTemplate<String, PaymentEvent> kafkaTemplate;

    public void send(PaymentEvent event) {
        log.info("Publishing payment event {}", event);

        kafkaTemplate.send("payment-events", event);

        log.info("Published successfully");
    }
}
