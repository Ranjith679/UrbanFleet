package com.urbanfleet.order_service.clients;

import com.urbanfleet.order_service.dto.MenuItemResponse;
import com.urbanfleet.order_service.dto.RestaurantResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(name = "restaurant-service", url = "http://localhost:8083")
public interface RestaurantClient {

    @GetMapping("/internal/restaurants/{id}")
    RestaurantResponse getRestaurant(
            @PathVariable UUID id
    );

    @GetMapping("/internal/menu/{id}")
    MenuItemResponse getMenuItem(
            @PathVariable UUID id
    );
}
