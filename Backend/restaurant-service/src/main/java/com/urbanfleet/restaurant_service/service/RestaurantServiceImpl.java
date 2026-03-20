package com.urbanfleet.restaurant_service.service;

import com.urbanfleet.restaurant_service.dto.RestaurantRequest;
import com.urbanfleet.restaurant_service.dto.RestaurantResponse;
import com.urbanfleet.restaurant_service.model.Restaurant;
import com.urbanfleet.restaurant_service.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RestaurantServiceImpl implements RestaurantService{

    private final RestaurantRepository repository;
    @Override
    public RestaurantResponse create(RestaurantRequest request, List<String> imageUrls) {
        Restaurant restaurant = Restaurant.builder()
                .name(request.getName())
                .description(request.getDescription())
                .address(request.getAddress())
                .city(request.getCity())
                .ownerId(request.getOwnerId())
                .imageUrls(imageUrls != null ? imageUrls : new ArrayList<>())
                .build();

        Restaurant saved = repository.save(restaurant);

        return mapToResponse(saved);
    }

    @Override
    public RestaurantResponse mapToResponse(Restaurant restaurant) {
        RestaurantResponse response = new RestaurantResponse();

        response.setId(restaurant.getId());
        response.setName(restaurant.getName());
        response.setDescription(restaurant.getDescription());
        response.setAddress(restaurant.getAddress());
        response.setCity(restaurant.getCity());
        response.setOwnerId(restaurant.getOwnerId());
        response.setImageUrls(restaurant.getImageUrls());

        return response;
    }
}