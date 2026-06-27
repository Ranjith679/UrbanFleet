package com.urbanfleet.restaurant_service.contoller;

import com.urbanfleet.restaurant_service.dto.RestaurantResponse;
import com.urbanfleet.restaurant_service.service.RestaurantService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/internal/restaurants")
public class InternalRestaurantController {

    @Autowired
    private RestaurantService service;

    @GetMapping("/{id}")
    public RestaurantResponse getRestaurant(
            @PathVariable UUID id
    ) {
        return service.getById(id);
    }
}
