package com.urbanfleet.events.delivery;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeliveryRejectedEvent {

    private UUID deliveryId;

    private UUID orderId;

    private UUID riderId;

    private String reason; // optional
}