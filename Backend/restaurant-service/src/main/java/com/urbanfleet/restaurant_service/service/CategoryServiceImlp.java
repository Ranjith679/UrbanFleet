package com.urbanfleet.restaurant_service.service;


import com.urbanfleet.restaurant_service.dto.CategoryRequest;
import com.urbanfleet.restaurant_service.dto.CategoryResponse;
import com.urbanfleet.restaurant_service.exceptions.CategoryAlreadyExistException;
import com.urbanfleet.restaurant_service.model.Category;
import com.urbanfleet.restaurant_service.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImlp implements CategoryService {

    private final CategoryRepository repository;

    public CategoryResponse create(CategoryRequest request) {

        if (repository.existsByNameIgnoreCase(request.getName())) {
            throw new CategoryAlreadyExistException("Category already exists");
        }

        Category category = new Category();
        category.setName(request.getName());

        repository.save(category);

        return mapToResponse(category);
    }

    public List<CategoryResponse> getAll() {

        return repository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    private CategoryResponse mapToResponse(Category category) {

        CategoryResponse res = new CategoryResponse();
        res.setId(category.getId());
        res.setName(category.getName());

        return res;
    }

}
