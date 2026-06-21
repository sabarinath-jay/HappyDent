package com.happydent.dao.impl;

import com.happydent.dao.PatientDao;
import com.happydent.exception.ResourceNotFoundException;
import com.happydent.model.Patient;
import com.happydent.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class JpaPatientDao implements PatientDao {

    private final PatientRepository repo;

    @Override
    public Patient save(Patient patient) {
        return repo.save(patient);
    }

    @Override
    public List<Patient> findAll(String search) {
        Specification<Patient> spec = Specification.where(null);
        if (StringUtils.hasText(search)) {
            String like = "%" + search.toLowerCase() + "%";
            spec = spec.and((root, query, cb) -> cb.or(
                cb.like(cb.lower(root.get("name")), like),
                cb.like(root.get("phone"), "%" + search + "%")
            ));
        }
        return repo.findAll(spec, Sort.by(Sort.Direction.DESC, "createdAt"));
    }

    @Override
    public Optional<Patient> findById(UUID id) {
        return repo.findById(id);
    }

    @Override
    public Patient update(UUID id, Patient data) {
        Patient existing = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found: " + id));
        existing.setName(data.getName());
        existing.setPhone(data.getPhone());
        existing.setEmail(data.getEmail());
        existing.setDob(data.getDob());
        existing.setBloodGroup(data.getBloodGroup());
        existing.setAddress(data.getAddress());
        existing.setAllergies(data.getAllergies());
        existing.setMedicalHistory(data.getMedicalHistory());
        return repo.save(existing);
    }

    @Override
    public void delete(UUID id) {
        if (!repo.existsById(id)) {
            throw new ResourceNotFoundException("Patient not found: " + id);
        }
        repo.deleteById(id);
    }
}
