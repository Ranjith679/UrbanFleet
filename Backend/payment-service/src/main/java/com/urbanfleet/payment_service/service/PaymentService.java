package com.urbanfleet.payment_service.service;

import com.stripe.model.Event;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import com.urbanfleet.payment_service.constants.PaymentStatus;
import com.urbanfleet.payment_service.dto.PaymentRequest;
import com.urbanfleet.payment_service.dto.PaymentResponse;
import com.urbanfleet.payment_service.entity.Payment;
import com.urbanfleet.payment_service.kafka.PaymentEvent;
import com.urbanfleet.payment_service.kafka.PaymentEventProducer;
import com.urbanfleet.payment_service.repository.PaymentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.stripe.model.Event;
import com.stripe.net.Webhook;

@Service
@Slf4j
public class PaymentService {


    @Autowired
    PaymentRepository repository;

    @Autowired
    PaymentEventProducer producer;

    public PaymentResponse create(PaymentRequest request) throws Exception {

        PaymentIntentCreateParams params =
                PaymentIntentCreateParams.builder()
                        // Stripe expects paise
                        .setAmount((long)(request.getAmount() * 100))
                        .setCurrency("inr")
                        // Automatically choose payment methods (UPI, cards , Wallets ,Bak payemnts)
                        .setAutomaticPaymentMethods(
                                PaymentIntentCreateParams.AutomaticPaymentMethods.builder()
                                        // Enable automatic payment methods
                                        .setEnabled(true)
                                        // usually stripe wants to redirect user after payment , for postman testing we disable it
                                        .setAllowRedirects(PaymentIntentCreateParams.AutomaticPaymentMethods.AllowRedirects.NEVER).build()
                        )
                        .build();

        PaymentIntent intent = PaymentIntent.create(params);

        Payment payment = new Payment();

        payment.setOrderId(request.getOrderId());
        payment.setAmount(request.getAmount());
        payment.setStripePaymentId(intent.getId());
        payment.setStatus(PaymentStatus.PENDING);

        repository.save(payment);

        System.out.println(intent.getId() + " , "+ intent.getClientSecret());
        return new PaymentResponse(
                intent.getId(),
                intent.getClientSecret()
        );
    }


    public void processWebhook(String payload, String signature) {

        try {

            // Verify webhook is really from Stripe
            Event event = Webhook.constructEvent(payload, signature, "whsec_d3700d61732b20b0d89b96ed5f49db53754c5d7e33ae2826a016fbbf00cca671");

            log.info("Received Stripe event: {}", event.getType());

            // Payment success
            if ("payment_intent.succeeded".equals(event.getType())) {
                handleSuccess(event);
            }

            // Payment failed
            else if ("payment_intent.payment_failed".equals(event.getType())) {
                handleFailure(event);
            }

        } catch (Exception e) {

            log.error("Webhook processing failed", e);
        }
    }

    // Handle successful payment
    private void handleSuccess(Event event) {


        PaymentIntent intent = (PaymentIntent) event.getDataObjectDeserializer().getObject().orElseThrow();

        String stripePaymentId = intent.getId();

        Payment payment = repository.findByStripePaymentId(stripePaymentId).orElseThrow(() -> new RuntimeException("Payment not found"));

        // Update DB
        payment.setStatus(PaymentStatus.SUCCESS);

        repository.save(payment);

        // Send Kafka event
        producer.send(new PaymentEvent("payment.success", payment.getOrderId()));

        log.info("Payment successful for order {}", payment.getOrderId());
    }

    // Handle failed payment
    private void handleFailure(Event event) {

        PaymentIntent intent = (PaymentIntent) event.getDataObjectDeserializer().getObject().orElseThrow();

        String stripePaymentId = intent.getId();

        Payment payment = repository.findByStripePaymentId(stripePaymentId).orElseThrow(() -> new RuntimeException("Payment not found"));

        payment.setStatus(PaymentStatus.FAILED);

        repository.save(payment);

        producer.send(new PaymentEvent("payment.failed", payment.getOrderId()));

        log.info("Payment failed for order {}", payment.getOrderId());
    }
}
