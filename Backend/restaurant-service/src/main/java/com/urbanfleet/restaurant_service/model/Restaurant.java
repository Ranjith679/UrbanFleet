package com.urbanfleet.restaurant_service.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "restaurants")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Restaurant {

    /*
    Entities can use validations annotations but in industry approach it's DTO which should handle
    API rules (validations) . Entities should represents just DB structure like Entity , Id , columns
    and relationship.
     */

    @Id
    @GeneratedValue  //
    @Column(columnDefinition = "UUID") // In postgres there is column type as UUID which store unique random ID without it hibernate create it as VARCHAR -> slow
    private UUID id;

    @NotBlank(message = "Name is required")
    @Size(max = 100)
    @Column(nullable = false)
    private String name;

    @NotBlank(message = "Description is required")
    @Size(max = 500)
    @Column(nullable = false)
    private String description;

    @NotBlank(message = "Address is required")
    @Size(max = 255)
    @Column(nullable = false)
    private String address;

    @NotBlank(message = "City is required")
    @Size(max = 100)
    @Column(nullable = false)
    private String city;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "owner_id", nullable = false)
    private UUID ownerId;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    // Automatically set timestamps unless we give while inserting
    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    // Auto assign while updating unless we give
    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
