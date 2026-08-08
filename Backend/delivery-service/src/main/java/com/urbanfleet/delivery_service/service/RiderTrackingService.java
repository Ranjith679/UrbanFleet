package com.urbanfleet.delivery_service.service;

import com.urbanfleet.delivery_service.entity.RiderTracking;
import com.urbanfleet.delivery_service.repository.RiderTrackingRepository;
import com.urbanfleet.events.delivery.RiderLocationUpdatedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class RiderTrackingService {

    private final RiderTrackingRepository repository;

    public void updateLocation(RiderLocationUpdatedEvent event) {

        RiderTracking tracking = repository
                .findByOrderId(event.getOrderId())
                .orElseGet(RiderTracking::new);

        tracking.setOrderId(event.getOrderId());
        tracking.setRiderId(event.getRiderId());
        tracking.setLatitude(event.getLatitude());
        tracking.setLongitude(event.getLongitude());
        tracking.setUpdatedAt(LocalDateTime.now());

        repository.save(tracking);
    }
}