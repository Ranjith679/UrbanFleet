package com.urbanfleet.order_service.controller;


import com.urbanfleet.order_service.dto.RestaurantLocationResponse;
import com.urbanfleet.order_service.service.OrderService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/internal/order")
public class OrderInternalController {

    private OrderService service;

    public OrderInternalController(OrderService service) {
        this.service = service;
    }

    @GetMapping("/{orderId}/restaurant-location")
    public RestaurantLocationResponse getRestaurantLocation(
            @PathVariable UUID orderId){

        return service.getRestaurantLocation(orderId);
    }
}
