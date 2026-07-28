package com.urbanfleet.restaurant_service.kafka;


import com.urbanfleet.events.menuItem.MenuItemEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MenuItemEventProducer {

    private final KafkaTemplate<String, MenuItemEvent> kafkaTemplate;

    private static final String TOPIC = "menuItems-events";

    public void sendMenuItemEvent(MenuItemEvent event) {
        kafkaTemplate.send(TOPIC, event);
    }
}
