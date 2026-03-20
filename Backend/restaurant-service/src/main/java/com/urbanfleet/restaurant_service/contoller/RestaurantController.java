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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("/api/v1/restaurant")
@RequiredArgsConstructor
public class RestaurantController {

    private final MinioService minioService;
    private final RestaurantService service;


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


}
