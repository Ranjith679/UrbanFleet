package com.urbanfleet.delivery_service.kafka;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;


public class PaymentEvent {

    private String eventType;

    private UUID orderId;


    public PaymentEvent() {
    }

    public PaymentEvent(String eventType, UUID orderId) {
        this.eventType = eventType;
        this.orderId = orderId;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public UUID getOrderId() {
        return orderId;
    }

    public void setOrderId(UUID orderId) {
        this.orderId = orderId;
    }
}
