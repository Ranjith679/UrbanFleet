package com.urbanfleet.delivery_service.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;

import java.util.UUID;

@Entity
@Data
public class Delivery {

    @Id
    @GeneratedValue
    private UUID id;

    // Order id coming from Order Service
    private UUID orderId;

    // Rider assigned
    private UUID riderId;

    // Current delivery status
    private DeliveryStatus status;
}
