package com.urbanfleet.restaurant_service.repository;

import com.urbanfleet.restaurant_service.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;
import java.util.UUID;

public interface CategoryRepository extends JpaRepository<Category, UUID> {
    boolean existsByNameIgnoreCase(String name);
    Category findByName(String name);
}
