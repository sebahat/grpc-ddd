package com.example.authservice.application.dto;

public record AuthResponse(
        String userId,
        String username,
        String email,
        String role,
        String token
) {
}