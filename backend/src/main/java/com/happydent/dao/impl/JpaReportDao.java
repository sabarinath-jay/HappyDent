package com.happydent.dao.impl;

import com.happydent.dao.ReportDao;
import com.happydent.exception.ResourceNotFoundException;
import com.happydent.model.Report;
import com.happydent.repository.ReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class JpaReportDao implements ReportDao {

    private final ReportRepository repo;

    @Override
    public Report save(Report report) {
        return repo.save(report);
    }

    @Override
    public List<Report> findAll(String search) {
        if (StringUtils.hasText(search)) {
            return repo.searchWithPatient(search);
        }
        return repo.findAllWithPatient();
    }

    @Override
    public Optional<Report> findById(UUID id) {
        return repo.findById(id);
    }

    @Override
    public void delete(UUID id) {
        if (!repo.existsById(id)) {
            throw new ResourceNotFoundException("Report not found: " + id);
        }
        repo.deleteById(id);
    }
}
