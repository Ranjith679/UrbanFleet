package com.urbanfleet.delivery_service.controller;

import com.urbanfleet.delivery_service.entity.DeliveryAssignment;
import com.urbanfleet.delivery_service.repository.DeliveryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/deliveries")
@RequiredArgsConstructor
public class DeliveryController {

    private final DeliveryRepository deliveryRepository;

    // Get All Assignments
    @GetMapping
    public List<DeliveryAssignment> getAll() {
        return deliveryRepository.findAll();
    }
}
