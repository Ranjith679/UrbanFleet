package com.urbanfleet.delivery_service.controller;

import com.urbanfleet.delivery_service.dto.RiderLocationRequest;
import com.urbanfleet.delivery_service.entity.Rider;
import com.urbanfleet.delivery_service.repository.RiderRepository;
import com.urbanfleet.delivery_service.service.RiderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/riders")
@RequiredArgsConstructor
public class RiderController {

    private final RiderRepository riderRepository;
    private final RiderService service;


    // Create Rider
    @PostMapping
    public Rider create(@RequestBody Rider rider) {
        return riderRepository.save(rider);
    }

    // Get All Riders
    @GetMapping
    public List<Rider> getAll() {
        return riderRepository.findAll();
    }

    @PutMapping("/{id}/location")
    public ResponseEntity<Void> updateLocation(@PathVariable UUID id, @RequestBody RiderLocationRequest request) {

           service.updateLocation(id, request);

           return ResponseEntity.ok().build();
    }
}
