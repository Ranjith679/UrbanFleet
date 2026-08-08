package com.urbanfleet.delivery_service.repository;

import com.urbanfleet.delivery_service.entity.RiderTracking;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface RiderTrackingRepository
        extends JpaRepository<RiderTracking, UUID> {

    Optional<RiderTracking> findByOrderId(UUID orderId);
}