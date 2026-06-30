package com.urbanfleet.delivery_service.kafka;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryAssignedEvent {

    private UUID orderId;

    private UUID riderId;
}