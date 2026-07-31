package com.urbanfleet.delivery_service.repository;

import com.urbanfleet.delivery_service.entity.Rider;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RiderRepository
        extends JpaRepository<Rider, UUID> {

    List<Rider> findByAvailableTrue();
}
