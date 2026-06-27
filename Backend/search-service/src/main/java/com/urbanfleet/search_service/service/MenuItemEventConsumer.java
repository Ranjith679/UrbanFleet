package com.urbanfleet.search_service.service;

import com.urbanfleet.search_service.dto.MenuItemEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class MenuItemEventConsumer {

    @Autowired
    private SearchService searchService;

    @KafkaListener(
            topics = "menuItems.events",
            groupId = "search-service"
    )
    public void consumeMenuItem(MenuItemEvent event) {

        switch (event.getEventType()) {

            case "menu_item.created":
                searchService.handleMenuItemCreated(event);
                break;
        }
    }
}
