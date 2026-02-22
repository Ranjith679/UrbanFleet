package com.urbanfleet.auth_service.dto;

import jakarta.validation.constraints.NotBlank;

public class UserLoginDto {

    @NotBlank
    private String email;

    @NotBlank
    private String passwordHash;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }
}
