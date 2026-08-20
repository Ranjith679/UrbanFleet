package com.urbanfleet.notification_service.kafka;

import com.urbanfleet.events.delivery.*;
import com.urbanfleet.events.order.OrderEvent;
import com.urbanfleet.events.payment.PaymentEvent;
import com.urbanfleet.notification_service.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationEventConsumer {

    private final NotificationService notificationService;


    // =====================================================
    // ORDER EVENTS
    // =====================================================

    @KafkaListener(
            topics = "order-events",
            groupId = "notification-service"
    )
    public void consumeOrderEvent(OrderEvent event) {

        log.info(
                "Received OrderEvent: {}",
                event.getEventType()
        );

        if ("order.created".equals(event.getEventType())) {

            notificationService.orderCreated(
                    event.getOrderId()
            );
        }
    }


    // =====================================================
    // PAYMENT EVENTS
    // =====================================================

    @KafkaListener(
            topics = "payment-events",
            groupId = "notification-service"
    )
    public void consumePaymentEvent(PaymentEvent event) {

        log.info(
                "Received PaymentEvent: {}",
                event.getEventType()
        );

        switch (event.getEventType()) {

            case "payment.success" ->
                    notificationService.paymentSuccessful(
                            event.getOrderId()
                    );

            case "payment.failed" ->
                    notificationService.paymentFailed(
                            event.getOrderId()
                    );

            default ->
                    log.info(
                            "Ignoring PaymentEvent: {}",
                            event.getEventType()
                    );
        }
    }


    // =====================================================
    // DELIVERY EVENTS
    // =====================================================

    @KafkaListener(
            topics = "delivery-events",
            groupId = "notification-service"
    )
    public void consumeDeliveryEvent(Object event) {

        if (event instanceof DeliveryAssignedEvent e) {

            log.info(
                    "Received DeliveryAssignedEvent: {}",
                    e.getOrderId()
            );

            notificationService.deliveryAssigned(
                    e.getOrderId()
            );

        } else if (event instanceof DeliveryAcceptedEvent e) {

            log.info(
                    "Received DeliveryAcceptedEvent: {}",
                    e.getOrderId()
            );

            notificationService.deliveryAccepted(
                    e.getOrderId()
            );

        } else if (event instanceof DeliveryRejectedEvent e) {

            log.info(
                    "Received DeliveryRejectedEvent: {}",
                    e.getOrderId()
            );

            notificationService.deliveryRejected(
                    e.getOrderId()
            );

        } else if (event instanceof DeliveryPickedUpEvent e) {

            log.info(
                    "Received DeliveryPickedUpEvent: {}",
                    e.getOrderId()
            );

            notificationService.orderPickedUp(
                    e.getOrderId()
            );

        } else if (event instanceof DeliveryCompletedEvent e) {

            log.info(
                    "Received DeliveryCompletedEvent: {}",
                    e.getOrderId()
            );

            notificationService.orderDelivered(
                    e.getOrderId()
            );

        } else if (event instanceof DeliveryCancelledEvent e) {

            log.info(
                    "Received DeliveryCancelledEvent: {}",
                    e.getOrderId()
            );

            notificationService.deliveryCancelled(
                    e.getOrderId()
            );

        } else {

            log.warn(
                    "Unknown delivery event: {}",
                    event.getClass().getName()
            );
        }
    }
}