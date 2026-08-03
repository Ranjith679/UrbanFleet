package com.urbanfleet.delivery_service.controller;

import com.urbanfleet.delivery_service.dto.DeliveryTrackingResponse;
import com.urbanfleet.delivery_service.entity.Delivery;
import com.urbanfleet.delivery_service.repository.DeliveryRepository;
import com.urbanfleet.delivery_service.service.DeliveryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/deliveries")
@RequiredArgsConstructor
public class DeliveryController {

    private final DeliveryRepository deliveryRepository;
    private final DeliveryService deliveryService;
    private final DeliveryService service;

    // Get All Assignments
    @GetMapping
    public List<Delivery> getAll() {
        return deliveryRepository.findAll();
    }

    @PutMapping("/{deliveryId}/accept")
    public void accept(
            @PathVariable UUID deliveryId) {

        service.accept(deliveryId);
    }

    @PutMapping("/{deliveryId}/reject")
    public void reject(
            @PathVariable UUID deliveryId) {

        service.reject(deliveryId);
    }

    @PostMapping("/{deliveryId}/pickup")
    public void pickup(
            @PathVariable UUID deliveryId){

        deliveryService.pickup(deliveryId);
    }

    @PostMapping("/{deliveryId}/complete")
    public void complete(
            @PathVariable UUID deliveryId){

        deliveryService.complete(deliveryId);
    }

    @GetMapping("/track/{orderId}")
    public ResponseEntity<DeliveryTrackingResponse> trackOrder(
            @PathVariable UUID orderId) {

        return ResponseEntity.ok(
                deliveryService.trackOrder(orderId)
        );
    }
}