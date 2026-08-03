package com.urbanfleet.delivery_service.dto;

import com.urbanfleet.delivery_service.constants.DeliveryStatus;

import java.util.UUID;

public class DeliveryTrackingResponse {

    private UUID orderId;
    private UUID riderId;

    private Double riderLatitude;
    private Double riderLongitude;

    private DeliveryStatus deliveryStatus;

    private Double customerLatitude;

    private Double customerLongitude;

    private Double remainingDistanceKm;

    private Integer estimatedArrivalMinutes;

    public DeliveryTrackingResponse() {
    }

    public DeliveryTrackingResponse(
            UUID orderId,
            UUID riderId,
            Double riderLatitude,
            Double riderLongitude,
            DeliveryStatus deliveryStatus) {

        this.orderId = orderId;
        this.riderId = riderId;
        this.riderLatitude = riderLatitude;
        this.riderLongitude = riderLongitude;
        this.deliveryStatus = deliveryStatus;
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

    public Double getRiderLatitude() {
        return riderLatitude;
    }

    public void setRiderLatitude(Double riderLatitude) {
        this.riderLatitude = riderLatitude;
    }

    public Double getRiderLongitude() {
        return riderLongitude;
    }

    public void setRiderLongitude(Double riderLongitude) {
        this.riderLongitude = riderLongitude;
    }

    public DeliveryStatus getDeliveryStatus() {
        return deliveryStatus;
    }

    public void setDeliveryStatus(DeliveryStatus deliveryStatus) {
        this.deliveryStatus = deliveryStatus;
    }

    public Double getCustomerLatitude() {
        return customerLatitude;
    }

    public void setCustomerLatitude(Double customerLatitude) {
        this.customerLatitude = customerLatitude;
    }

    public Double getCustomerLongitude() {
        return customerLongitude;
    }

    public void setCustomerLongitude(Double customerLongitude) {
        this.customerLongitude = customerLongitude;
    }

    public Double getRemainingDistanceKm() {
        return remainingDistanceKm;
    }

    public void setRemainingDistanceKm(Double remainingDistanceKm) {
        this.remainingDistanceKm = remainingDistanceKm;
    }

    public Integer getEstimatedArrivalMinutes() {
        return estimatedArrivalMinutes;
    }

    public void setEstimatedArrivalMinutes(Integer estimatedArrivalMinutes) {
        this.estimatedArrivalMinutes = estimatedArrivalMinutes;
    }
}