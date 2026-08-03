package com.urbanfleet.order_service.kafka;

import com.urbanfleet.events.delivery.DeliveryAcceptedEvent;
import com.urbanfleet.events.delivery.DeliveryAssignedEvent;
import com.urbanfleet.events.delivery.DeliveryCancelledEvent;
import com.urbanfleet.events.delivery.DeliveryCompletedEvent;
import com.urbanfleet.events.delivery.DeliveryPickedUpEvent;
import com.urbanfleet.events.delivery.DeliveryRejectedEvent;
import com.urbanfleet.order_service.constants.OrderAction;
import com.urbanfleet.order_service.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;


@Service
@KafkaListener(
        topics = "delivery-events",
        groupId = "order-service"
)
public class DeliveryEventConsumer {

    private final OrderService orderService;

    private static final Logger log = LoggerFactory.getLogger(DeliveryEventConsumer.class);


    public DeliveryEventConsumer(OrderService orderService) {
        this.orderService = orderService;
    }

    @KafkaHandler
    public void consume(DeliveryAssignedEvent event) {

        log.info("Delivery Assigned : {}", event.getOrderId());

        orderService.processAction(
                event.getOrderId(),
                OrderAction.ASSIGN_DELIVERY
        );
    }

    @KafkaHandler
    public void consume(DeliveryAcceptedEvent event) {

        log.info("Delivery Accepted : {}", event.getOrderId());

        orderService.processAction(
                event.getOrderId(),
                OrderAction.DELIVERY_ACCEPTED
        );
    }

    @KafkaHandler
    public void consume(DeliveryRejectedEvent event) {

        log.info("Delivery Rejected : {}", event.getOrderId());

        // No OrderStatus change.
        // Only logging.
    }

    @KafkaHandler
    public void consume(DeliveryPickedUpEvent event) {

        log.info("Picked Up : {}", event.getOrderId());

        orderService.processAction(
                event.getOrderId(),
                OrderAction.PICKUP_ORDER
        );
    }

    @KafkaHandler
    public void consume(DeliveryCompletedEvent event) {

        log.info("Completed : {}", event.getOrderId());

        orderService.processAction(
                event.getOrderId(),
                OrderAction.COMPLETE
        );
    }

    @KafkaHandler
    public void consume(DeliveryCancelledEvent event) {

        log.info("Delivery Cancelled : {}", event.getOrderId());

        orderService.processAction(
                event.getOrderId(),
                OrderAction.CANCEL
        );
    }
}