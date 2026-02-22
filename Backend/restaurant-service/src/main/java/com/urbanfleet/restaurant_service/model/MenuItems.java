package com.urbanfleet.restaurant_service.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "menu_items")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MenuItems {

    @Id
    @GeneratedValue
    @UuidGenerator  // explicitly mentioning UUID generation strategy . no need modern spring auto apply
    @Column(updatable = false, nullable = false)
    private UUID id;

    // Many records of menu items --> 1 restaurant id in FK column
    @ManyToOne(fetch = FetchType.LAZY , cascade = CascadeType.REMOVE)
    /*
     when i load an entity from a DB , should hibernate also load related entity right away ?
     the decision = fetchtype

     EARLY - load the related entry right away
     select * from menu_items where id = ?
     select * from restaurant where id = ?(from menu_item)

     LAZY - Only when needed or asked anywhere
     select *

     if a entity has many relations one simple entry fetching makes 5 -10 queries run
     slow , memory , cpu heavy -> low performance
     */

    /*
    if i delete restaurant what happens to menu items data ?

    depends on how configured :
    1. CaseCadeType.REMOVE => if restaurant record deleted ,delete related menu items records
    2.CascadeType.NULL => if restaurent record deleted , set the related FK column records to NULL
    3. Not mention => throes error

     */
    @JoinColumn(name = "restaurant_id", nullable = false)
    private Restaurant restaurant;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(length = 500)
    private String description;

    @Column(nullable = false, precision = 10, scale = 2)  // precision digits of whole number , scale after decimal digits
    private BigDecimal price;   // industry approach do not use double for price . rouding error

    @Column(name = "image_url")
    private String imageUrl;

    @Column(nullable = false)
    private boolean available = true;

    @ManyToMany
    @JoinTable(
            name = "menu_item_category",
            joinColumns = @JoinColumn(name = "menu_item_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )

    //creates middle table for many-many . add join column , other side of column
    private Set<Category> categories = new HashSet<>();
}

/*
 Relationships :

 @OneToMany in a entity means --> one record ID of this table pointed many records in other related
  table
 restarent id 1 --> menu item 1 = 1 , menu item 2 =1


 @ManyToMany in a entity means --> 1 record ID of this table pointed to many records of other related
 table and similarly one record ID of the other table pointed to many of this records .
One MenuItem → can belong to many Category
One Category → can have many MenuItem

 So usually in DB we use middle table contains both columns of FK relations

HOW TO SAVE DATA
 MenuItem pizza = new MenuItem();
pizza.setName("Pizza");

Category veg = categoryRepository.findByName("Veg");
Category spicy = categoryRepository.findByName("Spicy");
pizza.getCategories().add(veg);
pizza.getCategories().add(spicy);
menuItemRepository.save(pizza);

Do not use relationships in another microservice

student - cousres : many to many
order - product : many to many
owner - restaurant : many to one

 */