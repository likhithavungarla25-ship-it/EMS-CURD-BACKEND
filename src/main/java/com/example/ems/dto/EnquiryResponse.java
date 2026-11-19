package com.example.ems.dto;


import com.example.ems.enums.EnquiryStatus;

import java.time.LocalDateTime;

public record EnquiryResponse(
        Long id,
        String name,
        String email,
        String phone,
        String message,
        String city,
        EnquiryStatus status,
        LocalDateTime createdAt
) {
}
