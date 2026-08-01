package com.urbanfleet.delivery_service.scheduler;

import com.urbanfleet.delivery_service.constants.DeliveryStatus;
import com.urbanfleet.delivery_service.entity.Delivery;
import com.urbanfleet.delivery_service.repository.DeliveryRepository;
import com.urbanfleet.delivery_service.service.DeliveryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class DeliveryTimeoutScheduler {

    private final DeliveryRepository deliveryRepository;

    private final DeliveryService deliveryService;

    @Scheduled(fixedRate = 1000000)
    public void checkAssignmentTimeout() {

        List<Delivery> deliveries =
                deliveryRepository.findByStatus(DeliveryStatus.ASSIGNED);

        for (Delivery delivery : deliveries) {

            if (delivery.getAssignedAt() == null) {
                continue;
            }

            long seconds =
                    Duration.between(
                                    delivery.getAssignedAt(),
                                    LocalDateTime.now())
                            .getSeconds();

            if (seconds >= 30) {

                log.warn(
                        "Delivery {} timed out after {} seconds",
                        delivery.getId(),
                        seconds);

                deliveryService.assignNextRider(delivery);
            }
        }
    }
}
