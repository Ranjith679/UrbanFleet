package com.urbanfleet.restaurant_service.service;

import com.urbanfleet.restaurant_service.dto.CategoryRequest;
import com.urbanfleet.restaurant_service.dto.CategoryResponse;

import java.util.List;

public interface CategoryService {
    List<CategoryResponse> getAll();
    CategoryResponse create(CategoryRequest request);
}
