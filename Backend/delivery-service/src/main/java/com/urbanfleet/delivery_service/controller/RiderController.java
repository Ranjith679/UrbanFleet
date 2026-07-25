package com.urbanfleet.delivery_service.controller;

import com.urbanfleet.delivery_service.entity.Rider;
import com.urbanfleet.delivery_service.repository.RiderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/riders")
@RequiredArgsConstructor
public class RiderController {

    private final RiderRepository riderRepository;

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
}
