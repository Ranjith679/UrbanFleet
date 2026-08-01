package com.urbanfleet.events.delivery;

import java.util.UUID;

public class DeliveryAcceptedEvent {

    private UUID orderId;
    private UUID riderId;

    public DeliveryAcceptedEvent() {
    }

    public DeliveryAcceptedEvent(UUID orderId, UUID riderId) {
        this.orderId = orderId;
        this.riderId = riderId;
    }

    public UUID getOrderId() {
        return orderId;
    }

    public void setOrderId(UUID orderId) {
        this.orderId = orderId;
    }

    public UUID getRiderId() {
        return riderId;
    }

    public void setRiderId(UUID riderId) {
        this.riderId = riderId;
    }
}