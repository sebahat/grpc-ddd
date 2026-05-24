package com.example.authservice.application.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
        @NotBlank(message = "username is required")
        String username,

        @Email(message = "email must be valid")
        @NotBlank(message = "email is required")
        String email,

        @Size(min = 6, message = "password must be at least 6 characters")
        @NotBlank(message = "password is required")
        String password
) {
}