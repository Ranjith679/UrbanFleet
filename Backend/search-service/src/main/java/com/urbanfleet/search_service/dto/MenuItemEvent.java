package com.urbanfleet.search_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MenuItemEvent {

    private String eventType; // menu_item.created / menu_item.updated
    private String restaurantId;
    private String menuItemId;
    private String name;
    private Double price;
}
