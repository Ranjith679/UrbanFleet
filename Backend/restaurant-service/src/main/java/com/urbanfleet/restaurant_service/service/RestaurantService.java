package com.urbanfleet.restaurant_service.service;

import com.urbanfleet.restaurant_service.dto.RestaurantRequest;
import com.urbanfleet.restaurant_service.dto.RestaurantResponse;
import com.urbanfleet.restaurant_service.model.Restaurant;

import java.util.List;

public interface RestaurantService {
    RestaurantResponse create(RestaurantRequest restaurantRequest, List<String> imageUrls);
    RestaurantResponse mapToResponse(Restaurant restaurant);
}
