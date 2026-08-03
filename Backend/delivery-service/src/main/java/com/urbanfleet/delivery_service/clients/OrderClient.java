package com.urbanfleet.delivery_service.clients;

import com.urbanfleet.delivery_service.dto.CustomerLocationResponse;
import com.urbanfleet.delivery_service.dto.RestaurantLocationResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(name = "order-service")
public interface OrderClient {

    @GetMapping("/internal/order/{orderId}/restaurant-location")
    RestaurantLocationResponse getRestaurantLocation(
            @PathVariable UUID orderId);

    @GetMapping("/internal/order/{orderId}/customer-location")
    CustomerLocationResponse getCustomerLocation(
            @PathVariable UUID orderId
    );

}
