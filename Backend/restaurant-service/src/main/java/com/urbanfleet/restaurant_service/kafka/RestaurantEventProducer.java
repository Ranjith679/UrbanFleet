package com.urbanfleet.restaurant_service.kafka;


import com.urbanfleet.events.restaurant.RestaurantEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RestaurantEventProducer {

    private final KafkaTemplate<String, RestaurantEvent> kafkaTemplate;

    private static final String TOPIC = "restaurant-events";

    public void sendEvent(RestaurantEvent event) {
        kafkaTemplate.send(TOPIC, event);
    }
}
