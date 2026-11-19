package com.example.ems.dto;

public record AuthResponse(
        String message,
        String accessToken,
        String refreshToken,
        String tokenType,
        long expiresIn,
        Long adminId,
        String name,
        String email,
        String role
) {}
