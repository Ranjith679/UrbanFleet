package com.urbanfleet.delivery_service.kafka;

import com.urbanfleet.delivery_service.service.DeliveryService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class DeliveryConsumer {

    private final DeliveryService deliveryService;

    public DeliveryConsumer(DeliveryService deliveryService) {
        this.deliveryService = deliveryService;
    }

    Logger log = LoggerFactory.getLogger(DeliveryConsumer.class);

    // Listen when payment succeeds
    @KafkaListener(topics = "payment-events", groupId = "delivery-service")
    public void consume(PaymentEvent event) {

        log.info("Received payment event {}", event);
        // Ignore failed payments
        if (!event.getEventType().equals("payment.success")) {
            return;
        }

        // Assign a rider
        deliveryService.assign(event.getOrderId());
    }
}
