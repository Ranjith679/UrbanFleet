package com.urbanfleet.payment_service.controller;

import com.urbanfleet.payment_service.dto.PaymentRequest;
import com.urbanfleet.payment_service.dto.PaymentResponse;
import com.urbanfleet.payment_service.kafka.PaymentEvent;
import com.urbanfleet.payment_service.kafka.PaymentEventProducer;
import com.urbanfleet.payment_service.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService service;

    private final PaymentEventProducer producer;

    @PostMapping
    public PaymentResponse create(@RequestBody PaymentRequest request) throws Exception {
        return service.create(request);
    }

    // Receives webhook from Stripe
    @PostMapping("/webhook")
    public ResponseEntity<Void> webhook(

            // Raw JSON sent by Stripe
            @RequestBody String payload,

            // Signature header sent by Stripe
            @RequestHeader("Stripe-Signature") String signature

    ) {

        System.out.println("WEBHOOK HIT");
        service.processWebhook(payload, signature);

        return ResponseEntity.ok().build();
    }


    // test for webhook
    @PostMapping("/test-success/{orderId}")
    public void testSuccess(@PathVariable UUID orderId) {

        System.out.println("sending kafka event");

        producer.send(new PaymentEvent("payment.success", orderId));
    }
}
