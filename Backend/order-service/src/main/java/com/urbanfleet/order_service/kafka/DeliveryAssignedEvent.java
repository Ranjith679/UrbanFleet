package com.urbanfleet.order_service.kafka;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
public class DeliveryAssignedEvent {

    private UUID orderId;

    private UUID riderId;

    public DeliveryAssignedEvent(UUID riderId, UUID orderId) {
        this.riderId = riderId;
        this.orderId = orderId;
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