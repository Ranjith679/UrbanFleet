package com.urbanfleet.payment_service.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.stripe.model.Event;
import com.stripe.model.PaymentIntent;
import com.stripe.net.Webhook;
import com.stripe.param.PaymentIntentCreateParams;
import com.urbanfleet.payment_service.constants.PaymentStatus;
import com.urbanfleet.payment_service.dto.PaymentRequest;
import com.urbanfleet.payment_service.dto.PaymentResponse;
import com.urbanfleet.payment_service.entity.Payment;
import com.urbanfleet.payment_service.kafka.PaymentEventProducer;
import com.urbanfleet.payment_service.repository.PaymentRepository;
import com.urbanfleet.events.payment.PaymentEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentService {

    // Database operations
    private final PaymentRepository repository;

    // Kafka producer
    private final PaymentEventProducer producer;

    // Used to read webhook JSON
    private final ObjectMapper objectMapper = new ObjectMapper();

    // Stripe webhook secret
    private static final String WEBHOOK_SECRET =
            "whsec_e52f0aef97e65d833267377435139f365a515e2c0139d13cdd793fc58b02690d";

    // ----------------------------------------------------
    // Create PaymentIntent
    // ----------------------------------------------------

    public PaymentResponse create(PaymentRequest request) throws Exception {

        PaymentIntentCreateParams params =
                PaymentIntentCreateParams.builder()

                        // Amount in paise
                        .setAmount((long) (request.getAmount() * 100))

                        .setCurrency("inr")

                        // Store Order Id inside Stripe
                        .putMetadata("orderId",
                                request.getOrderId().toString())

                        // Enable card payments
                        .setAutomaticPaymentMethods(
                                PaymentIntentCreateParams
                                        .AutomaticPaymentMethods
                                        .builder()
                                        .setEnabled(true)
                                        .setAllowRedirects(
                                                PaymentIntentCreateParams
                                                        .AutomaticPaymentMethods
                                                        .AllowRedirects
                                                        .NEVER)
                                        .build()
                        )

                        .build();

        PaymentIntent intent = PaymentIntent.create(params);

        Payment payment = new Payment();

        payment.setOrderId(request.getOrderId());
        payment.setAmount(request.getAmount());
        payment.setStripePaymentId(intent.getId());
        payment.setStatus(PaymentStatus.PENDING);

        repository.save(payment);

        log.info(intent.getId());
        return new PaymentResponse(
                intent.getId(),
                intent.getClientSecret()
        );
    }

    // ----------------------------------------------------
    // Stripe Webhook
    // ----------------------------------------------------

    public void processWebhook(String payload, String signature) {

        try {

            // Verify Stripe signature
            Event event = Webhook.constructEvent(payload, signature, WEBHOOK_SECRET);

            log.info("Stripe Event : {}", event.getType());

            // Only interested in payment success
            if (!event.getType().equals("payment_intent.succeeded")) {
                return;
            }

            handleSuccess(payload);

        } catch (Exception e) {

            log.error("Webhook failed", e);
        }

    }

    // ----------------------------------------------------
    // Handle Payment Success
    // ----------------------------------------------------

    private void handleSuccess(String payload) throws Exception {

        // Read webhook JSON
        JsonNode root = objectMapper.readTree(payload);

        // PaymentIntent Id
        String paymentIntentId = root.path("data").path("object").path("id").asText();

        log.info("PaymentIntent = {}", paymentIntentId);

        // Find payment
        Payment payment = repository
                .findByStripePaymentId(paymentIntentId)
                .orElseThrow(() ->
                        new RuntimeException("Payment not found"));

        // Already processed?
        if (payment.getStatus() == PaymentStatus.SUCCESS) {

            log.info("Already processed");

            return;
        }

        // Update database
        payment.setStatus(PaymentStatus.SUCCESS);

        repository.save(payment);

        // Notify Order Service
        producer.send(
                new PaymentEvent(
                        "payment.success",
                        payment.getOrderId()
                )
        );

        log.info("Kafka Event Published");

    }

}