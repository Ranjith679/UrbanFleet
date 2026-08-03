package com.urbanfleet.order_service.controller;

import com.urbanfleet.order_service.constants.OrderAction;
import com.urbanfleet.order_service.dto.CreateOrderRequest;
import com.urbanfleet.order_service.dto.CustomerLocationResponse;
import com.urbanfleet.order_service.dto.RestaurantLocationResponse;
import com.urbanfleet.order_service.entity.Order;
import com.urbanfleet.order_service.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/order")
public class OrderController {

    @Autowired
    private OrderService service;

    @PostMapping("/create")
    public Order create(@RequestBody CreateOrderRequest request) {
        return service.create(request);
    }

    @GetMapping("/{id}")
    public Order getOrder(@PathVariable UUID id) {

        return service.getOrder(id);
    }

    @GetMapping("/customer/{customerId}")
    public List<Order> getOrdersByCustomer(
            @PathVariable String customerId) {

        return service.getOrdersByCustomer(customerId);
    }

    @PutMapping("/{orderId}/status")
    public ResponseEntity<Order> updateStatus(
            @PathVariable UUID orderId,
            @RequestParam OrderAction event
    ) {

        return ResponseEntity.ok(
                service.updateStatus(
                        orderId,
                        event
                )
        );
    }

    @GetMapping("/internal/order/{orderId}/customer-location")
    public CustomerLocationResponse getCustomerLocation(
            @PathVariable UUID orderId) {

        return service.getCustomerLocation(orderId);
    }
}
