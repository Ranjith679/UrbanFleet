package com.urbanfleet.auth_service.dto;

import com.urbanfleet.auth_service.constants.Roles;

import java.time.LocalDate;

public class UserResDto {

    private String name;

    private String email;

    private Roles role;

    private LocalDate updatedDate;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Enum getRole() {
        return role;
    }

    public void setRole(Roles role) {
        this.role = role;
    }

    public LocalDate getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(LocalDate updatedDate) {
        this.updatedDate = updatedDate;
    }
}
