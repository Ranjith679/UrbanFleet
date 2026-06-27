package com.urbanfleet.payment_service.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class PaymentRequest {

    private UUID orderId;

    private Double amount;
}