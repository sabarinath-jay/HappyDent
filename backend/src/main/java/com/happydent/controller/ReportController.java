package com.happydent.controller;

import com.happydent.dto.ReportResponse;
import com.happydent.model.Report;
import com.happydent.service.FileStorageService;
import com.happydent.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService service;
    private final FileStorageService fileStorage;

    @GetMapping
    public ResponseEntity<List<ReportResponse>> list(
            @RequestParam(required = false) String search) {
        return ResponseEntity.ok(service.list(search));
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ReportResponse> upload(
            @RequestParam UUID patientId,
            @RequestParam String title,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) String reportDate,
            @RequestParam(required = false) MultipartFile file) throws IOException {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.upload(patientId, title, description, reportDate, file));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Serves uploaded report files.
     * {filename:.+} ensures the dot and extension are included in the path variable.
     * PDFs and images are served inline so the browser renders them directly.
     */
    @GetMapping("/files/{patientId}/{filename:.+}")
    public ResponseEntity<Resource> download(
            @PathVariable String patientId,
            @PathVariable String filename) throws MalformedURLException {
        Path filePath = fileStorage.resolve(patientId + "/" + filename);
        Resource resource = new UrlResource(filePath.toUri());
        if (!resource.exists()) {
            return ResponseEntity.notFound().build();
        }
        String contentType = detectContentType(filename);
        boolean isInline = contentType.startsWith("image/") || contentType.equals("application/pdf");
        String disposition = (isInline ? "inline" : "attachment") + "; filename=\"" + filename + "\"";
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, disposition)
                .header(HttpHeaders.CACHE_CONTROL, "no-cache")
                .contentType(MediaType.parseMediaType(contentType))
                .body(resource);
    }

    private String detectContentType(String filename) {
        String f = filename.toLowerCase();
        if (f.endsWith(".pdf"))  return "application/pdf";
        if (f.endsWith(".jpg") || f.endsWith(".jpeg")) return "image/jpeg";
        if (f.endsWith(".png"))  return "image/png";
        if (f.endsWith(".gif"))  return "image/gif";
        if (f.endsWith(".doc"))  return "application/msword";
        if (f.endsWith(".docx")) return "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
        return "application/octet-stream";
    }
}
