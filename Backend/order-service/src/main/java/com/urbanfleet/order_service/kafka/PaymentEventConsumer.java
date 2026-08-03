package com.urbanfleet.order_service.kafka;

import com.urbanfleet.events.payment.PaymentEvent;
import com.urbanfleet.order_service.constants.OrderAction;
import com.urbanfleet.order_service.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@KafkaListener(
        topics = "payment-events",
        groupId = "order-service"
)
public class PaymentEventConsumer {

    private final OrderService orderService;

    public PaymentEventConsumer(OrderService orderService) {
        this.orderService = orderService;
    }

    private static final Logger log = LoggerFactory.getLogger(PaymentEventConsumer.class);

    @KafkaHandler
    public void consume(PaymentEvent event) {

        log.info("Received PaymentEvent : {}", event);

        switch (event.getEventType()) {

            case "payment.success" ->

                    orderService.processAction(
                            event.getOrderId(),
                            OrderAction.CONFIRM_PAYMENT
                    );

            case "payment.failed" ->

                    orderService.processAction(
                            event.getOrderId(),
                            OrderAction.CANCEL
                    );

            default ->

                    log.warn("Unknown payment event {}", event.getEventType());
        }
    }
}