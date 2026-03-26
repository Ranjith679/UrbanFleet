package com.urbanfleet.restaurant_service.service;

import com.urbanfleet.restaurant_service.dto.MenuItemRequest;
import com.urbanfleet.restaurant_service.dto.MenuItemResponse;
import com.urbanfleet.restaurant_service.dto.RestaurantRequest;
import com.urbanfleet.restaurant_service.dto.RestaurantResponse;
import com.urbanfleet.restaurant_service.model.Restaurant;

import java.util.List;
import java.util.UUID;

public interface RestaurantService {
    RestaurantResponse create(RestaurantRequest restaurantRequest, List<String> imageUrls);
    RestaurantResponse mapToResponse(Restaurant restaurant);
    RestaurantResponse getById(UUID id);
    List<RestaurantResponse> getRestaurants(String city);
    RestaurantResponse update(UUID id, RestaurantRequest request);
    void delete(UUID id);


}
