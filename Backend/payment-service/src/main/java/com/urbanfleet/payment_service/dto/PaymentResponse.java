package com.urbanfleet.payment_service.dto;

import com.urbanfleet.payment_service.constants.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentResponse {

    private String paymentIntentId;

    private String clientSecret;
}
