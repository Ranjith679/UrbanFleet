package com.urbanfleet.restaurant_service.contoller;

import com.urbanfleet.restaurant_service.dto.MenuItemResponse;
import com.urbanfleet.restaurant_service.service.MenuItemsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/internal/menu")
public class InternalMenuController {

    @Autowired
    private MenuItemsService service;

    @GetMapping("/{id}")
    public MenuItemResponse getMenuItem(
            @PathVariable UUID id
    ) {
        return service.getMenuByMenuId(id);
    }
}