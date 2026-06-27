package com.urbanfleet.order_service.constants;

// This enum represents all possible order states
public enum OrderStatus {

    // Order just created
    CREATED,

    // Payment completed
    PAID,

    // Restaurant started preparing food
    PREPARING,

    // Delivery partner picked up order
    PICKED_UP,

    // Order delivered successfully
    DELIVERED,

    // Order cancelled
    CANCELLED
}
