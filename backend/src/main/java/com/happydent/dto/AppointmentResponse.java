package com.happydent.dto;

import com.happydent.model.Appointment;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public record AppointmentResponse(
        UUID id,
        String name,
        String phone,
        String email,
        String service,
        LocalDate preferredDate,
        String preferredTime,
        String message,
        String status,
        String notes,
        LocalDateTime createdAt
) {
    public static AppointmentResponse from(Appointment a) {
        return new AppointmentResponse(
                a.getId(), a.getName(), a.getPhone(), a.getEmail(),
                a.getService(), a.getPreferredDate(), a.getPreferredTime(), a.getMessage(),
                a.getStatus(), a.getNotes(), a.getCreatedAt()
        );
    }
}
