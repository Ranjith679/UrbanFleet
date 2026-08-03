package com.urbanfleet.order_service.dto;

public class CustomerLocationResponse {

    private Double latitude;
    private Double longitude;

    public CustomerLocationResponse() {
    }

    public CustomerLocationResponse(Double latitude,
                                    Double longitude) {

        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }
}