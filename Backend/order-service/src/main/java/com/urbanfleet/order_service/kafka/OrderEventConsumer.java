package com.urbanfleet.order_service.kafka;

import com.urbanfleet.order_service.constants.OrderStatus;
import com.urbanfleet.order_service.entity.Order;
import com.urbanfleet.order_service.repositories.OrderRepository;
import com.urbanfleet.events.delivery.DeliveryAssignedEvent;
import com.urbanfleet.events.payment.PaymentEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;


@Service
public class OrderEventConsumer {

    @Autowired
    OrderRepository orderRepository;

    private static final Logger log = LoggerFactory.getLogger(OrderEventConsumer.class);

    @KafkaListener(topics = "payment-events", groupId = "order-service")
    public void consumePayment(PaymentEvent event) {

        log.info("Received payment event from payment-service");

        Order order = orderRepository.findById(event.getOrderId()).orElseThrow(()->new RuntimeException("Order not found"));

        order.setStatus(OrderStatus.PAID);

        orderRepository.save(order);
    }

    @KafkaListener(topics = "delivery-events", groupId = "order-service")
    public void consumeDelivery(DeliveryAssignedEvent event ,  @Header(KafkaHeaders.RECEIVED_PARTITION) int partition,
                                @Header(KafkaHeaders.OFFSET) long offset) {

        log.info("Offset={}, Partition={}", offset, partition);
        log.info("Event={}", event);


        log.info("Received delivery event: {}", event);
        log.info("Order ID from event: {}", event.getOrderId());

        Order order = orderRepository.findById(event.getOrderId())
                .orElseThrow(() -> new RuntimeException(
                        "Order not found: " + event.getOrderId()));

        order.setStatus(OrderStatus.PICKED_UP);

        orderRepository.save(order);
    }
}
