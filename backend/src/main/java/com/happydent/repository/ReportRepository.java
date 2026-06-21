package com.happydent.repository;

import com.happydent.model.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface ReportRepository extends JpaRepository<Report, UUID> {

    @Query("SELECT r FROM Report r JOIN FETCH r.patient ORDER BY r.createdAt DESC")
    List<Report> findAllWithPatient();

    @Query("SELECT r FROM Report r JOIN FETCH r.patient p " +
           "WHERE LOWER(r.title) LIKE LOWER(CONCAT('%',:q,'%')) " +
           "   OR LOWER(p.name)  LIKE LOWER(CONCAT('%',:q,'%')) " +
           "ORDER BY r.createdAt DESC")
    List<Report> searchWithPatient(String q);
}
