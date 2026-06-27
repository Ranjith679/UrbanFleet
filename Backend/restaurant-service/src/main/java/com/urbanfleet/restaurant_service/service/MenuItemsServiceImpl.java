package com.urbanfleet.restaurant_service.service;

import com.urbanfleet.restaurant_service.dto.MenuItemEvent;
import com.urbanfleet.restaurant_service.dto.MenuItemRequest;
import com.urbanfleet.restaurant_service.dto.MenuItemResponse;
import com.urbanfleet.restaurant_service.exceptions.MenuItemNotFoundException;
import com.urbanfleet.restaurant_service.exceptions.RestaurantNotFoundException;
import com.urbanfleet.restaurant_service.kafka.MenuItemEventProducer;
import com.urbanfleet.restaurant_service.model.Category;
import com.urbanfleet.restaurant_service.model.MenuItems;
import com.urbanfleet.restaurant_service.model.Restaurant;
import com.urbanfleet.restaurant_service.repository.CategoryRepository;
import com.urbanfleet.restaurant_service.repository.MenuItemsRepository;
import com.urbanfleet.restaurant_service.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class MenuItemsServiceImpl implements MenuItemsService{

    private final MenuItemsRepository menuItemsRepository;
    private final RestaurantRepository restaurantRepository;
    private final MinioService minioService;
    private final CategoryRepository categoryRepository;
    private final MenuItemEventProducer producer;

    @Override
    public MenuItemResponse createMenuItem(UUID restaurantId, MenuItemRequest request , String imageUrl) {

        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new RestaurantNotFoundException("Restaurant not found"));

        MenuItems item = new MenuItems();
        item.setName(request.getName());
        item.setDescription(request.getDescription());
        item.setPrice(request.getPrice());
        item.setImageUrl(imageUrl);
        item.setAvailable(true);
        item.setRestaurant(restaurant);
        Category Drinks = categoryRepository.findByName("Drinks");
        Category Meal = categoryRepository.findByName("Meal");
        if(Drinks != null && Meal !=null) {
            item.setCategories(Set.of(Drinks, Meal));
        }
        menuItemsRepository.save(item);

        MenuItemEvent event = new MenuItemEvent(
                "menu_item.created",
                item.getRestaurant().getId().toString(),
                item.getId().toString(),
                item.getName(),
                item.getPrice().doubleValue()
        );

        log.info("Sending menu item event: {}", event);
        producer.sendMenuItemEvent(event);

        return mapToResponse(item);
    }


    @Override
    public List<MenuItemResponse> getMenuByRestaurant(UUID restaurantId) {

        List<MenuItems> items = menuItemsRepository.findByRestaurantId(restaurantId);

        return items.stream()
                .map(this::mapToResponse)
                .toList();
    }

    public MenuItemResponse getMenuByMenuId(UUID id){
        MenuItems menu = menuItemsRepository.findById(id).orElseThrow(()->new MenuItemNotFoundException("Menu item not found for the id"));


            MenuItemResponse response = new MenuItemResponse();

            response.setId(menu.getId());
            response.setName(menu.getName());
            response.setDescription(menu.getDescription());
            response.setImageUrl(menu.getImageUrl());
            response.setPrice(menu.getPrice());
            response.setRestaurantId(menu.getRestaurant().getId());
            response.setAvailable(menu.isAvailable());

        return response;
    }

    @Override
    public MenuItemResponse update(UUID id, MenuItemRequest request) {

        MenuItems item = menuItemsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Menu item not found"));

        item.setName(request.getName());
        item.setDescription(request.getDescription());
        item.setPrice(request.getPrice());


        menuItemsRepository.save(item);

        MenuItemEvent event = new MenuItemEvent(
                "menu_item.updated",
                item.getRestaurant().getId().toString(),
                item.getId().toString(),
                item.getName(),
                item.getPrice().doubleValue()
        );

        log.info("Sending menu item event: {}", event);
        producer.sendMenuItemEvent(event);

        return mapToResponse(item);
    }

    @Override
    public void delete(UUID id) {

        MenuItems item = menuItemsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Menu item not found"));

        menuItemsRepository.delete(item);
    }




    // Mapper
    public MenuItemResponse mapToResponse(MenuItems items){
        MenuItemResponse response = new MenuItemResponse();

        response.setId(items.getId());
        response.setName(items.getName());
        response.setDescription(items.getDescription());
        response.setPrice(items.getPrice());

        String url ;
        try {
            url = minioService.getImageUrl(items.getImageUrl());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        response.setImageUrl(url);
        response.setAvailable(items.isAvailable());
        response.setRestaurantId(items.getRestaurant().getId());

        return response;
    }
}
