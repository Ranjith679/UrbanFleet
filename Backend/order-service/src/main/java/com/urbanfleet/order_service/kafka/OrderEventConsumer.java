package com.urbanfleet.order_service.kafka;

import com.urbanfleet.order_service.constants.OrderStatus;
import com.urbanfleet.order_service.entity.Order;
import com.urbanfleet.order_service.repositories.OrderRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class OrderEventConsumer {

    @Autowired
    OrderRepository orderRepository;

    @KafkaListener(topics = "payment-events", groupId = "order-service")
    public void consumePayment(PaymentEvent event) {

        log.info("Received payment event from payment-service");

        Order order = orderRepository.findById(event.getOrderId()).orElseThrow(()->new RuntimeException("Order not found"));

        order.setStatus(OrderStatus.PAID);

        orderRepository.save(order);
    }

    @KafkaListener(topics = "delivery-events", groupId = "order-service")
    public void consumeDelivery(DeliveryAssignedEvent event) {

        // Find order
        Order order = orderRepository.findById(event.getOrderId())
                .orElseThrow();

        // Update order status
        order.setStatus(OrderStatus.PICKED_UP);

        orderRepository.save(order);
    }
}
