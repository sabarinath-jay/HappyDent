package com.happydent.dto;

import com.happydent.model.Patient;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public record PatientResponse(
        UUID id,
        String name,
        String phone,
        String email,
        LocalDate dob,
        String bloodGroup,
        String address,
        String allergies,
        String medicalHistory,
        LocalDateTime createdAt
) {
    public static PatientResponse from(Patient p) {
        return new PatientResponse(
                p.getId(), p.getName(), p.getPhone(), p.getEmail(),
                p.getDob(), p.getBloodGroup(), p.getAddress(),
                p.getAllergies(), p.getMedicalHistory(), p.getCreatedAt()
        );
    }
}
