package com.urbanfleet.delivery_service.kafka;

import com.urbanfleet.delivery_service.dto.PaymentEvent;
import com.urbanfleet.delivery_service.service.DeliveryService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeliveryConsumer {

    private final DeliveryService deliveryService;

    // Listen when payment succeeds
    @KafkaListener(topics = "payment-events", groupId = "delivery-service")
    public void consume(PaymentEvent event) {

        // Ignore failed payments
        if (!event.getEventType().equals("payment.success")) {
            return;
        }

        // Assign a rider
        deliveryService.assign(event.getOrderId());
    }
}
