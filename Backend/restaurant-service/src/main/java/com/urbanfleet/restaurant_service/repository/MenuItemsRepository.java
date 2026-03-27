package com.urbanfleet.restaurant_service.repository;

import com.urbanfleet.restaurant_service.model.MenuItems;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MenuItemsRepository extends JpaRepository<MenuItems, UUID> {
    Optional<MenuItems> findById(UUID id);
    List<MenuItems> findByRestaurantId(UUID restaurantId);
}

