package com.happydent.service;

import com.happydent.dao.AppointmentDao;
import com.happydent.dto.AppointmentRequest;
import com.happydent.dto.AppointmentResponse;
import com.happydent.exception.ResourceNotFoundException;
import com.happydent.model.Appointment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AppointmentService {

    private final AppointmentDao dao;
    private final NotificationService notifications;

    public AppointmentResponse book(AppointmentRequest req) {
        Appointment appt = Appointment.builder()
                .name(req.name())
                .phone(req.phone())
                .email(req.email())
                .service(req.service())
                .preferredDate(parseDate(req.preferredDate()))
                .preferredTime(req.preferredTime())
                .message(req.message())
                .status("pending")
                .build();

        Appointment saved = dao.save(appt);
        notifications.notifyNewAppointment(saved);
        return AppointmentResponse.from(saved);
    }

    public List<AppointmentResponse> list(String status, String search) {
        return dao.findAll(status, search)
                .stream()
                .map(AppointmentResponse::from)
                .toList();
    }

    public AppointmentResponse updateStatus(UUID id, String status) {
        validateStatus(status);
        return AppointmentResponse.from(dao.updateStatus(id, status));
    }

    public void delete(UUID id) {
        dao.delete(id);
    }

    public AppointmentResponse getById(UUID id) {
        return dao.findById(id)
                .map(AppointmentResponse::from)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found: " + id));
    }

    private LocalDate parseDate(String date) {
        if (!StringUtils.hasText(date)) return null;
        try {
            return LocalDate.parse(date);
        } catch (Exception e) {
            return null;
        }
    }

    private void validateStatus(String status) {
        if (!List.of("pending", "confirmed", "completed", "cancelled").contains(status)) {
            throw new IllegalArgumentException("Invalid status: " + status);
        }
    }
}
