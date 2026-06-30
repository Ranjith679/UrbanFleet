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

    // true means rider can take order
    private boolean available;
}