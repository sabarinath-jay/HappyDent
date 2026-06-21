package com.happydent.service;

import com.happydent.dao.PatientDao;
import com.happydent.dao.ReportDao;
import com.happydent.dto.ReportResponse;
import com.happydent.exception.ResourceNotFoundException;
import com.happydent.model.Patient;
import com.happydent.model.Report;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final ReportDao reportDao;
    private final PatientDao patientDao;
    private final FileStorageService fileStorage;

    @Value("${server.port:8080}")
    private String serverPort;

    public List<ReportResponse> list(String search) {
        return reportDao.findAll(search).stream().map(ReportResponse::from).toList();
    }

    public ReportResponse upload(UUID patientId, String title, String description,
                                  String reportDate, MultipartFile file) throws IOException {
        Patient patient = patientDao.findById(patientId)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found: " + patientId));

        String fileUrl = null, fileName = null, fileType = null;
        if (file != null && !file.isEmpty()) {
            String storagePath = fileStorage.store(file, patientId.toString());
            fileUrl = "/api/reports/files/" + storagePath;
            fileName = file.getOriginalFilename();
            fileType = file.getContentType();
        }

        Report report = Report.builder()
                .patient(patient)
                .title(title)
                .description(description)
                .fileUrl(fileUrl)
                .fileName(fileName)
                .fileType(fileType)
                .reportDate(parseDate(reportDate))
                .build();

        return ReportResponse.from(reportDao.save(report));
    }

    public void delete(UUID id) {
        Report report = reportDao.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Report not found: " + id));
        if (StringUtils.hasText(report.getFileUrl())) {
            String storagePath = report.getFileUrl().replace("/api/reports/files/", "");
            fileStorage.delete(storagePath);
        }
        reportDao.delete(id);
    }

    public Report getRaw(UUID id) {
        return reportDao.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Report not found: " + id));
    }

    private LocalDate parseDate(String date) {
        if (!StringUtils.hasText(date)) return null;
        try {
            return LocalDate.parse(date);
        } catch (Exception e) {
            return null;
        }
    }
}
