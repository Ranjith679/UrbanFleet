package com.urbanfleet.search_service.model;

import lombok.Data;

@Data
public class MenuItem {

    private String id;
    private String name;
    private Double price;
    private String category;
}
