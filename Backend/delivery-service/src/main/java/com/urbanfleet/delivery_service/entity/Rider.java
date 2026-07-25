package com.urbanfleet.delivery_service.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;

import java.util.UUID;

@Entity
@Data
public class Rider {

    @Id
    @GeneratedValue
    private UUID id;

    private String name;

    // Current latitude of rider
    private Double latitude;

    // Current longitude of rider
    private Double longitude;

    // Can rider take new order?
    private boolean available;
}