package com.urbanfleet.order_service.clients;

import com.urbanfleet.order_service.dto.PaymentRequest;
import com.urbanfleet.order_service.dto.PaymentResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "payment-service")
public interface PaymentClient {

    @PostMapping("/payments")
    PaymentResponse createPayment(
            @RequestBody PaymentRequest request
    );
}
