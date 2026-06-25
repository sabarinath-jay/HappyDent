package com.happydent.controller;

import com.happydent.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/admin/settings")
@RequiredArgsConstructor
public class SettingsController {

    private final NotificationService notificationService;

    @Value("${app.notification.email.enabled:false}") private boolean emailEnabled;
    @Value("${spring.mail.username:}") private String emailFrom;
    @Value("${app.notification.email.to:}") private String emailTo;
    @Value("${app.notification.whatsapp.phone:}") private String waPhone;

    @GetMapping("/status")
    public ResponseEntity<Map<String, Object>> status() {
        return ResponseEntity.ok(Map.of(
            "emailEnabled", emailEnabled,
            "emailFrom", emailFrom,
            "emailTo", emailTo.isEmpty() ? emailFrom : emailTo,
            "waPhone", waPhone,
            "waEnabled", !waPhone.isEmpty(),
            "dbType", "PostgreSQL (Railway)"
        ));
    }

    @PostMapping("/test-email")
    public ResponseEntity<Map<String, String>> testEmail() {
        boolean sent = notificationService.sendTestEmail();
        if (sent) return ResponseEntity.ok(Map.of("message", "Test email sent to " + (emailTo.isEmpty() ? emailFrom : emailTo)));
        return ResponseEntity.status(500).body(Map.of("message", "Failed — check Gmail App Password is set in Railway env vars"));
    }
}
