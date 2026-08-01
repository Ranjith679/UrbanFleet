package com.urbanfleet.delivery_service.kafka;

import com.urbanfleet.events.delivery.DeliveryAcceptedEvent;
import com.urbanfleet.events.delivery.DeliveryAssignedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class DeliveryProducer {

    private final KafkaTemplate<String, DeliveryAssignedEvent> assignedTemplate;
    private final KafkaTemplate<String, DeliveryAcceptedEvent> acceptedTemplate;

    public DeliveryProducer(KafkaTemplate<String, DeliveryAssignedEvent> assignedTemplate,
                            KafkaTemplate<String, DeliveryAcceptedEvent> acceptedTemplate) {
        this.assignedTemplate = assignedTemplate;
        this.acceptedTemplate = acceptedTemplate;
    }

    public void sendAssigned(DeliveryAssignedEvent event) {

        // Publish delivery assignment event
        assignedTemplate.send("delivery-events", event);
    }

    public void sendAccepted(DeliveryAcceptedEvent event) {

        acceptedTemplate.send("delivery-events", event);

        log.info("Published DeliveryAcceptedEvent for order {}",
                event.getOrderId());
    }
}
