package com.urbanfleet.delivery_service.service;

import com.urbanfleet.delivery_service.dto.RiderLocationRequest;
import com.urbanfleet.delivery_service.entity.Rider;
import com.urbanfleet.delivery_service.repository.RiderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class RiderService {

    private final RiderRepository repository;

    public Rider updateLocation(
            UUID riderId,
            RiderLocationRequest request
    ) {

        Rider rider = repository.findById(riderId)
                .orElseThrow();

        rider.setLatitude(request.getLatitude());

        rider.setLongitude(request.getLongitude());

        rider.setLastLocationUpdate(LocalDateTime.now());

        log.info("Updated location for rider {}", rider.getId());

        return repository.save(rider);

    }
}