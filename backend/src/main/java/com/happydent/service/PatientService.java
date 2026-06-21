package com.happydent.service;

import com.happydent.dao.PatientDao;
import com.happydent.dto.PatientRequest;
import com.happydent.dto.PatientResponse;
import com.happydent.exception.ResourceNotFoundException;
import com.happydent.model.Patient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PatientService {

    private final PatientDao dao;

    public PatientResponse create(PatientRequest req) {
        Patient patient = toEntity(req);
        return PatientResponse.from(dao.save(patient));
    }

    public List<PatientResponse> list(String search) {
        return dao.findAll(search).stream().map(PatientResponse::from).toList();
    }

    public PatientResponse getById(UUID id) {
        return dao.findById(id)
                .map(PatientResponse::from)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found: " + id));
    }

    public PatientResponse update(UUID id, PatientRequest req) {
        return PatientResponse.from(dao.update(id, toEntity(req)));
    }

    public void delete(UUID id) {
        dao.delete(id);
    }

    private Patient toEntity(PatientRequest req) {
        return Patient.builder()
                .name(req.name())
                .phone(req.phone())
                .email(req.email())
                .dob(parseDate(req.dob()))
                .bloodGroup(req.bloodGroup())
                .address(req.address())
                .allergies(req.allergies())
                .medicalHistory(req.medicalHistory())
                .build();
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
