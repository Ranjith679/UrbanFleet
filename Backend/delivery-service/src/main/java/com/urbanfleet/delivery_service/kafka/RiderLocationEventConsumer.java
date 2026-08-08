package com.urbanfleet.delivery_service.kafka;

import com.urbanfleet.delivery_service.service.RiderTrackingService;
import com.urbanfleet.events.delivery.DeliveryAssignedEvent;
import com.urbanfleet.events.delivery.DeliveryAcceptedEvent;
import com.urbanfleet.events.delivery.DeliveryCancelledEvent;
import com.urbanfleet.events.delivery.DeliveryCompletedEvent;
import com.urbanfleet.events.delivery.DeliveryPickedUpEvent;
import com.urbanfleet.events.delivery.DeliveryRejectedEvent;
import com.urbanfleet.events.delivery.RiderLocationUpdatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@KafkaListener(
        topics = "delivery-events",
        groupId = "tracking-service"
)
@RequiredArgsConstructor
public class RiderLocationEventConsumer {

    private final RiderTrackingService trackingService;

    @KafkaHandler
    public void consume(RiderLocationUpdatedEvent event) {

        log.info(
                "Tracking location update | orderId={} | riderId={} | lat={} | lon={}",
                event.getOrderId(),
                event.getRiderId(),
                event.getLatitude(),
                event.getLongitude()
        );

        trackingService.updateLocation(event);
    }

    @KafkaHandler
    public void consume(DeliveryAssignedEvent event) {

        log.info(
                "Tracking received DeliveryAssignedEvent | orderId={}",
                event.getOrderId()
        );
    }

    @KafkaHandler
    public void consume(DeliveryAcceptedEvent event) {

        log.info(
                "Tracking received DeliveryAcceptedEvent | orderId={}",
                event.getOrderId()
        );
    }

    @KafkaHandler
    public void consume(DeliveryRejectedEvent event) {

        log.info(
                "Tracking received DeliveryRejectedEvent | orderId={}",
                event.getOrderId()
        );
    }

    @KafkaHandler
    public void consume(DeliveryPickedUpEvent event) {

        log.info(
                "Tracking received DeliveryPickedUpEvent | orderId={}",
                event.getOrderId()
        );
    }

    @KafkaHandler
    public void consume(DeliveryCompletedEvent event) {

        log.info(
                "Tracking received DeliveryCompletedEvent | orderId={}",
                event.getOrderId()
        );
    }

    @KafkaHandler
    public void consume(DeliveryCancelledEvent event) {

        log.info(
                "Tracking received DeliveryCancelledEvent | orderId={}",
                event.getOrderId()
        );
    }
}