package com.urbanfleet.restaurant_service.repository;

import com.urbanfleet.restaurant_service.model.Restaurant;
import jdk.jfr.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface RestaurantRepository extends JpaRepository<Restaurant , UUID> {

    List<Restaurant> findByCityIgnoreCase(String city);

}
