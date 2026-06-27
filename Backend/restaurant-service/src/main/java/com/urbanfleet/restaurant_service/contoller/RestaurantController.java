package com.urbanfleet.restaurant_service.contoller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.urbanfleet.restaurant_service.dto.RestaurantRequest;
import com.urbanfleet.restaurant_service.dto.RestaurantResponse;
import com.urbanfleet.restaurant_service.service.MinioService;
import com.urbanfleet.restaurant_service.service.RestaurantService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("/api/v1/restaurant")
@RequiredArgsConstructor
public class RestaurantController {

    private final MinioService minioService;
    private final RestaurantService service;


    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(value = "/upload",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<RestaurantResponse> create(
            @RequestPart("data") @Valid String data,
            @RequestPart("images") List<MultipartFile> images
    ) throws Exception {

        ObjectMapper mapper = new ObjectMapper();
        RestaurantRequest request = mapper.readValue(data, RestaurantRequest.class);

        List<String> imageUrls = minioService.uploadFiles(images);

        RestaurantResponse response = service.create(request, imageUrls);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<RestaurantResponse> getById(@PathVariable UUID id) {

        RestaurantResponse response = service.getById(id);

        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<RestaurantResponse>> getByCity(
            @RequestParam(required = false) String city) {

        List<RestaurantResponse> response = service.getRestaurants(city);

        return ResponseEntity.ok(response);
    }


    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<RestaurantResponse> update(
            @PathVariable UUID id,
            @RequestBody @Valid RestaurantRequest request) {

        RestaurantResponse response = service.update(id, request);

        return ResponseEntity.ok(response);
    }


    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {

        service.delete(id);

        return ResponseEntity.noContent().build();
    }


}
