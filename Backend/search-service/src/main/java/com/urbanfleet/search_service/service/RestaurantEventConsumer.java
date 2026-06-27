package com.urbanfleet.search_service.service;

import com.urbanfleet.search_service.dto.RestaurantEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;


@Component
@Slf4j
public class RestaurantEventConsumer {

    @Autowired
    private SearchService searchService;

    @KafkaListener(
            topics = "restaurant.events",
            groupId = "search-service",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void consume(RestaurantEvent event) {

        log.info("Received event: {}", event.getEventType());

        switch (event.getEventType()) {

            case "restaurant.created":
                searchService.handleRestaurantCreated(event);
                break;

            case "restaurant.updated":
                searchService.handleRestaurantUpdated(event);
                break;
        }
    }

}