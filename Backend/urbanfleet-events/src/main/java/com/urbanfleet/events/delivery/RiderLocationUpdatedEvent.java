package com.urbanfleet.events.delivery;

import java.util.UUID;

public class RiderLocationUpdatedEvent {

    private UUID orderId;
    private UUID riderId;
    private Double latitude;
    private Double longitude;

    public RiderLocationUpdatedEvent() {
    }

    public RiderLocationUpdatedEvent(
            UUID orderId,
            UUID riderId,
            Double latitude,
            Double longitude) {

        this.orderId = orderId;
        this.riderId = riderId;
        this.latitude = latitude;
        this.longitude = longitude;
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

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }
}