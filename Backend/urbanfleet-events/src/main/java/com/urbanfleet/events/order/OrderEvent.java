package com.urbanfleet.events.order;

import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;


@NoArgsConstructor
public class OrderEvent {

    public OrderEvent(String eventType, UUID orderId, UUID restaurantId, BigDecimal totalAmount, String status) {
        this.eventType = eventType;
        this.orderId = orderId;
        this.restaurantId = restaurantId;
        this.totalAmount = totalAmount;
        this.status = status;
    }

    private String eventType;

    private UUID orderId;

    private UUID restaurantId;

    private BigDecimal totalAmount;

    private String status;

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

    public UUID getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(UUID restaurantId) {
        this.restaurantId = restaurantId;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
