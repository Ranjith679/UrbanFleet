package com.urbanfleet.order_service.kafka;

import com.urbanfleet.order_service.dto.OrderEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderEventProducer {

    @Autowired
    private  KafkaTemplate<String, OrderEvent> kafkaTemplate;

    private static final String TOPIC = "order-events";

    public void send(OrderEvent event) {

        kafkaTemplate.send(TOPIC, event);
    }
}