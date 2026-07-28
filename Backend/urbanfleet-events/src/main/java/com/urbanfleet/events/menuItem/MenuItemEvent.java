package com.urbanfleet.events.menuItem;

import lombok.*;

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
