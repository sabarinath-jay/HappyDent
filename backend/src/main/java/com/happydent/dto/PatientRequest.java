package com.happydent.dto;

import jakarta.validation.constraints.NotBlank;

public record PatientRequest(
        @NotBlank String name,
        @NotBlank String phone,
        String email,
        String dob,
        String bloodGroup,
        String address,
        String allergies,
        String medicalHistory
) {}
