package com.urbanfleet.restaurant_service.service;

import com.urbanfleet.restaurant_service.dto.RestaurantRequest;
import com.urbanfleet.restaurant_service.dto.RestaurantResponse;
import com.urbanfleet.restaurant_service.exceptions.RestaurantNotFoundException;
import com.urbanfleet.restaurant_service.model.Restaurant;
import com.urbanfleet.restaurant_service.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class RestaurantServiceImpl implements RestaurantService{

    private final RestaurantRepository repository;
    private final MinioService minioService;

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

        List<String> urls = new ArrayList<>();        for (String fileName : restaurant.getImageUrls()){
            try {
                String url = minioService.getImageUrl(fileName);
                urls.add(url);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        response.setImageUrls(urls);
        return response;
    }

    @Override
    public RestaurantResponse getById(UUID id) {

        log.info("Fetching restaurant with id: {}", id);
        Restaurant restaurant = repository.findById(id)
                .orElseThrow(() -> new RestaurantNotFoundException("Restaurant not found with id: " + id));

        return mapToResponse(restaurant);
    }

    @Override
    public List<RestaurantResponse> getRestaurants(String city) {

        List<Restaurant> restaurants;

        if (city != null) {
            restaurants = repository.findByCityIgnoreCase(city);
        } else {
            restaurants = repository.findAll();
        }

        return restaurants.stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public RestaurantResponse update(UUID id, RestaurantRequest request) {

        log.info("Fetching restaurant with id: {}", id);
        Restaurant restaurant = repository.findById(id)
                .orElseThrow(() -> new RestaurantNotFoundException("Restaurant not found with id: " + id));

        restaurant.setName(request.getName());
        restaurant.setDescription(request.getDescription());
        restaurant.setAddress(request.getAddress());
        restaurant.setCity(request.getCity());
        restaurant.setOwnerId(request.getOwnerId());

        repository.save(restaurant);

        return mapToResponse(restaurant);
    }

    @Override
    public void delete(UUID id) {

        log.info("Fetching restaurant with id: {}", id);
        Restaurant restaurant = repository.findById(id)
                .orElseThrow(() -> new RestaurantNotFoundException("Restaurant not found with id: " + id));

        repository.delete(restaurant);
    }

}