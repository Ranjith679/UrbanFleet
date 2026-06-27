package com.urbanfleet.search_service.service;

import com.urbanfleet.search_service.dto.MenuItemEvent;
import com.urbanfleet.search_service.dto.RestaurantEvent;
import com.urbanfleet.search_service.model.MenuItem;
import com.urbanfleet.search_service.model.RestaurantDocument;
import com.urbanfleet.search_service.repository.RestaurantSearchRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@Slf4j
public class SearchService {

    @Autowired
    private RestaurantSearchRepository repository;

    public void handleRestaurantCreated(RestaurantEvent event) {
        RestaurantDocument doc = mapToDocument(event);
        log.info("Saving event of restaurant created in elastic search index");
        repository.save(doc);
    }

    public void handleRestaurantUpdated(RestaurantEvent event) {
        RestaurantDocument doc = mapToDocument(event);
        log.info("Saving event of restaurant updated in elastic search index");
        repository.save(doc);
    }

    public void handleMenuItemCreated(MenuItemEvent event) {

        RestaurantDocument restaurant =
                repository.findById(event.getRestaurantId())
                        .orElseThrow();

        MenuItem menuItem = new MenuItem();

        menuItem.setId(event.getMenuItemId());
        menuItem.setName(event.getName());
        menuItem.setPrice(event.getPrice());

        if (restaurant.getMenuItems() == null) {
            restaurant.setMenuItems(new ArrayList<>());
        }

        restaurant.getMenuItems().add(menuItem);

        repository.save(restaurant);

        log.info("Menu item added into Elasticsearch");
    }

    private RestaurantDocument mapToDocument(RestaurantEvent event) {
        RestaurantDocument doc = new RestaurantDocument();

        doc.setId(event.getId());
        doc.setName(event.getName());
        doc.setCity(event.getCity());
        doc.setAddress(event.getAddress());

        return doc;
    }
}