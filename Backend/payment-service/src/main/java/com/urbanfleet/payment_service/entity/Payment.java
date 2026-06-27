package com.urbanfleet.payment_service.entity;

import com.urbanfleet.payment_service.constants.PaymentStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

import java.util.UUID;

@Entity
@Data
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    // Which order this payment belongs to
    private UUID orderId;

    // Amount to pay
    private Double amount;

    // Stripe payment intent id
    private String stripePaymentId;

    // PENDING / SUCCESS / FAILED
    private PaymentStatus status;
}