package com.urbanfleet.delivery_service.kafka;

import com.urbanfleet.events.delivery.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class DeliveryEventProducer {

    private static final String TOPIC = "delivery-events";

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void publishAssigned(DeliveryAssignedEvent event) {

        kafkaTemplate.send(TOPIC, event.getOrderId().toString(), event);

        log.info("Published DeliveryAssignedEvent : {}", event);
    }

    public void publishAccepted(DeliveryAcceptedEvent event) {

        kafkaTemplate.send(TOPIC, event.getOrderId().toString(), event);

        log.info("Published DeliveryAcceptedEvent : {}", event);
    }

    public void publishRejected(DeliveryRejectedEvent event) {

        kafkaTemplate.send(TOPIC, event.getOrderId().toString(), event);

        log.info("Published DeliveryRejectedEvent : {}", event);
    }

    public void publishPickedUp(DeliveryPickedUpEvent event) {

        kafkaTemplate.send(TOPIC, event.getOrderId().toString(), event);

        log.info("Published DeliveryPickedUpEvent : {}", event);
    }

    public void publishCompleted(DeliveryCompletedEvent event) {

        kafkaTemplate.send(TOPIC, event.getOrderId().toString(), event);

        log.info("Published DeliveryCompletedEvent : {}", event);
    }

    public void publishCancelled(DeliveryCancelledEvent event) {

        kafkaTemplate.send(TOPIC, event.getOrderId().toString(), event);

        log.info("Published DeliveryCancelledEvent : {}", event);
    }

    public void publishLocationUpdated(RiderLocationUpdatedEvent event) {

        kafkaTemplate.send(TOPIC, event.getOrderId().toString(), event);

        log.info(
                "Published RiderLocationUpdatedEvent: orderId={}, riderId={}, lat={}, lon={}",
                event.getOrderId(),
                event.getRiderId(),
                event.getLatitude(),
                event.getLongitude()
        );
    }
}