package com.urbanfleet.order_service.service;

import com.urbanfleet.order_service.clients.PaymentClient;
import com.urbanfleet.order_service.clients.RestaurantClient;
import com.urbanfleet.order_service.constants.OrderAction;
import com.urbanfleet.order_service.constants.OrderStatus;
import com.urbanfleet.order_service.dto.*;
import com.urbanfleet.order_service.entity.Order;
import com.urbanfleet.order_service.entity.OrderItem;
import com.urbanfleet.order_service.kafka.OrderEventProducer;
import com.urbanfleet.order_service.repositories.OrderItemRepository;
import com.urbanfleet.order_service.repositories.OrderRepository;
import com.urbanfleet.events.order.OrderEvent;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class OrderService {


    @Autowired
    private  OrderRepository orderRepository;

    @Autowired
    private  OrderItemRepository itemRepository;

    @Autowired
    private  OrderEventProducer producer;


    @Autowired
    public OrderStateMachine stateMachine;

    @Autowired
    public RestaurantClient restaurantClient;
    @Autowired
    public PaymentClient paymentClient;


    private static final Logger log = LoggerFactory.getLogger(OrderService.class);

    public Order create(CreateOrderRequest request) {

        // Validate restaurant exists
        try {
            restaurantClient.getRestaurant(request.getRestaurantId());
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }

        // Create order object
        Order order = new Order();

        order.setCustomerId(request.getCustomerId());

        order.setRestaurantId(request.getRestaurantId());

        order.setStatus(OrderStatus.CREATED);

        order.setCreatedAt(LocalDateTime.now());

        // Temporary value for amount
        // Real total will be calculated below
        order.setTotalAmount(BigDecimal.ZERO);

        // Save order first to generate Order ID
        Order savedOrder = orderRepository.save(order);

        BigDecimal total = BigDecimal.ZERO;

        for (OrderItemRequest item : request.getItems()) {
            MenuItemResponse menuItem = restaurantClient.getMenuItem(item.getMenuItemId());

            OrderItem orderItem = new OrderItem();

            orderItem.setOrderId(savedOrder.getId());
            orderItem.setMenuItemId(item.getMenuItemId());
            orderItem.setName(menuItem.getName());
            orderItem.setQuantity(item.getQuantity());
            orderItem.setPrice(menuItem.getPrice().doubleValue());

            BigDecimal itemTotal = menuItem.getPrice().multiply(BigDecimal.valueOf(item.getQuantity()));

            total = total.add(itemTotal);

            itemRepository.save(orderItem);
        }

        savedOrder.setTotalAmount(total);

        orderRepository.save(savedOrder);

        // Publish Kafka event
        producer.send(
                new OrderEvent(
                        "order.created",
                        savedOrder.getId(),
                        savedOrder.getRestaurantId(),
                        savedOrder.getTotalAmount(),
                        savedOrder.getStatus().name()
                )
        );


        PaymentRequest payment = new PaymentRequest(savedOrder.getId(),savedOrder.getTotalAmount().doubleValue());
        paymentClient.createPayment(payment);

        return savedOrder;
    }

    // find order by order id
    public Order getOrder(UUID orderId) {

        return orderRepository.findById(orderId).orElseThrow(() -> new RuntimeException("Order not found"));
    }

    // get order by customer name
    public List<Order> getOrdersByCustomer(String customerId) {

        return orderRepository.findByCustomerId(customerId);
    }


    // update order status manually by order action
    public Order updateStatus(UUID orderId, OrderAction action) {

        // Get order from DB
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new RuntimeException("Order not found"));

        // Ask state machine for next valid state
        OrderStatus nextStatus = stateMachine.nextState(order.getStatus(), action);

        // Update order status
        order.setStatus(nextStatus);

        // Save to DB
        return orderRepository.save(order);
    }

    public RestaurantLocationResponse getRestaurantLocation(UUID orderId) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        RestaurantResponse restaurant =
                restaurantClient.getRestaurant(order.getRestaurantId());

        return new RestaurantLocationResponse(
                (restaurant.getLatitude() != null) ? restaurant.getLatitude() : 11.0168,
                (restaurant.getLongitude() != null ) ? restaurant.getLongitude() : 76.9558
        );
    }

    @Transactional
    public void processAction(UUID orderId, OrderAction action) {

        log.info("OrderId received = {}", orderId);

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        OrderStatus nextStatus =
                stateMachine.nextState(order.getStatus(), action);

        order.setStatus(nextStatus);

        orderRepository.save(order);

        log.info("Order {} -> {}", orderId, nextStatus);
    }


    public CustomerLocationResponse getCustomerLocation(UUID orderId) {

        orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        return new CustomerLocationResponse(
                11.0254,
                76.9647
        );
    }

}
