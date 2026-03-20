package com.urbanfleet.restaurant_service;

import com.urbanfleet.restaurant_service.model.Restaurant;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RestaurantServiceApplication {

	public static void main(String[] args) {
		Restaurant r = Restaurant.builder().name("Test").build();
		SpringApplication.run(RestaurantServiceApplication.class, args);

	}

}
