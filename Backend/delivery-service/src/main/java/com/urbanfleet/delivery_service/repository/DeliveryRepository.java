package com.urbanfleet.delivery_service.repository;

import com.urbanfleet.delivery_service.constants.DeliveryStatus;
import com.urbanfleet.delivery_service.entity.Delivery;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface DeliveryRepository
        extends JpaRepository<Delivery, UUID> {
    Optional<Delivery> findByRiderIdAndStatus(
            UUID riderId,
            DeliveryStatus status
    );

}
