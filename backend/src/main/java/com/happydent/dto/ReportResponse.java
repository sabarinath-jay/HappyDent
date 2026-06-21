package com.happydent.dto;

import com.happydent.model.Report;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public record ReportResponse(
        UUID id,
        UUID patientId,
        String patientName,
        String title,
        String description,
        String fileUrl,
        String fileName,
        String fileType,
        LocalDate reportDate,
        LocalDateTime createdAt
) {
    public static ReportResponse from(Report r) {
        return new ReportResponse(
                r.getId(),
                r.getPatient().getId(),
                r.getPatient().getName(),
                r.getTitle(),
                r.getDescription(),
                r.getFileUrl(),
                r.getFileName(),
                r.getFileType(),
                r.getReportDate(),
                r.getCreatedAt()
        );
    }
}
