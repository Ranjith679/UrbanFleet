package com.urbanfleet.restaurant_service.service;

import com.urbanfleet.restaurant_service.dto.MenuItemRequest;
import com.urbanfleet.restaurant_service.dto.MenuItemResponse;

import java.util.List;
import java.util.UUID;

public interface MenuItemsService {
    // Menu Item Service
    MenuItemResponse createMenuItem(UUID restaurantId, MenuItemRequest request , String imageUrl);
    List<MenuItemResponse> getMenuByRestaurant(UUID restaurantId);
    MenuItemResponse update(UUID id, MenuItemRequest request);
    void delete(UUID id);
}
