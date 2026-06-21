package com.happydent.dao;

import com.happydent.model.Appointment;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AppointmentDao {
    Appointment save(Appointment appointment);
    List<Appointment> findAll(String status, String search);
    Optional<Appointment> findById(UUID id);
    Appointment updateStatus(UUID id, String status);
    void delete(UUID id);
}
