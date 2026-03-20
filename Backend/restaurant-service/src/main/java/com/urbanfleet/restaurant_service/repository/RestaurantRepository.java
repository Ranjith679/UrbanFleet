package com.urbanfleet.restaurant_service.repository;

import com.urbanfleet.restaurant_service.model.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface RestaurantRepository extends JpaRepository<Restaurant , UUID> {
}
