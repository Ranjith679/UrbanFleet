package com.urbanfleet.delivery_service.service;

import com.urbanfleet.delivery_service.dto.RiderLocationRequest;
import com.urbanfleet.delivery_service.entity.Rider;
import com.urbanfleet.delivery_service.repository.RiderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

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

        return repository.save(rider);
    }
}