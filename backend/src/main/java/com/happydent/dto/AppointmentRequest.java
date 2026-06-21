package com.happydent.dto;

import jakarta.validation.constraints.NotBlank;

public record AppointmentRequest(
        @NotBlank String name,
        @NotBlank String phone,
        String email,
        String service,
        String preferredDate,
        String preferredTime,
        String message
) {}
