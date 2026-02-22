package com.urbanfleet.restaurant_service.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.awt.*;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "categories")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Category {

    @Id
    @GeneratedValue
    @UuidGenerator
    @Column(nullable = false, updatable = false)
    private UUID id;

    @Column(nullable = false, unique = true, length = 100)
    private String name;

    // optional back reference
    @ManyToMany(mappedBy = "categories")  // in industries using set for many to many to avoid duplicates and fast lookup
    private Set<MenuItem> menuItems = new HashSet<>();
}
