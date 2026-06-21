package com.happydent.dao;

import com.happydent.model.Patient;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PatientDao {
    Patient save(Patient patient);
    List<Patient> findAll(String search);
    Optional<Patient> findById(UUID id);
    Patient update(UUID id, Patient data);
    void delete(UUID id);
}
