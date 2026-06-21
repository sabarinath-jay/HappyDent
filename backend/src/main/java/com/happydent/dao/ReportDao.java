package com.happydent.dao;

import com.happydent.model.Report;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ReportDao {
    Report save(Report report);
    List<Report> findAll(String search);
    Optional<Report> findById(UUID id);
    void delete(UUID id);
}
