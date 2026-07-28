package com.urbanfleet.events.delivery;


import java.util.UUID;

public class DeliveryAssignedEvent {

    private UUID orderId;

    private UUID riderId;

    public DeliveryAssignedEvent() {
    }

    public DeliveryAssignedEvent(UUID orderId, UUID riderId) {
        this.orderId = orderId;
        this.riderId = riderId;
    }

    public UUID getOrderId() {
        return orderId;
    }

    public UUID getRiderId() {
        return riderId;
    }

    public void setOrderId(UUID orderId) {
        this.orderId = orderId;
    }

    public void setRiderId(UUID riderId) {
        this.riderId = riderId;
    }
}