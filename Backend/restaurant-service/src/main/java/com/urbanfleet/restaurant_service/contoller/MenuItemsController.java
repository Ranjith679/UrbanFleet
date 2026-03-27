package com.urbanfleet.restaurant_service.contoller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.urbanfleet.restaurant_service.dto.MenuItemRequest;
import com.urbanfleet.restaurant_service.dto.MenuItemResponse;
import com.urbanfleet.restaurant_service.service.MenuItemsService;
import com.urbanfleet.restaurant_service.service.MinioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/menu")
public class MenuItemsController {

    private final MenuItemsService service;
    private final MinioService minioService;
    // Menu REST API

    @PostMapping(value = "/uploadMenu/{id}",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<MenuItemResponse> createMenuItem(
            @PathVariable UUID id,
            @RequestPart("data") @Valid String data ,
            @RequestPart("images") MultipartFile images) throws Exception {

        ObjectMapper mapper = new ObjectMapper();
        MenuItemRequest request = mapper.readValue(data, MenuItemRequest.class);

        String imageUrl = minioService.uploadFile(images);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.createMenuItem(id, request , imageUrl));
    }

    @GetMapping("/{id}")
    public ResponseEntity<List<MenuItemResponse>> getMenu(@PathVariable UUID id) {

        return ResponseEntity.ok(service.getMenuByRestaurant(id));
    }

    @PutMapping("{menuItemId}")
    public ResponseEntity<MenuItemResponse> update(
            @PathVariable UUID menuItemId,
            @RequestBody MenuItemRequest request) {

        return ResponseEntity.ok(service.update(menuItemId, request));
    }

    @DeleteMapping("{menuItemId}")
    public ResponseEntity<Void> deleteMenu(@PathVariable UUID menuItemId) {

        service.delete(menuItemId);

        return ResponseEntity.noContent().build();
    }
}
