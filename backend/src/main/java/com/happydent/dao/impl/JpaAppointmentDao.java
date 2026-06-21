package com.happydent.dao.impl;

import com.happydent.dao.AppointmentDao;
import com.happydent.exception.ResourceNotFoundException;
import com.happydent.model.Appointment;
import com.happydent.repository.AppointmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class JpaAppointmentDao implements AppointmentDao {

    private final AppointmentRepository repo;

    @Override
    public Appointment save(Appointment appointment) {
        return repo.save(appointment);
    }

    @Override
    public List<Appointment> findAll(String status, String search) {
        Specification<Appointment> spec = Specification.where(null);

        if (StringUtils.hasText(status) && !"all".equalsIgnoreCase(status)) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("status"), status));
        }
        if (StringUtils.hasText(search)) {
            String like = "%" + search.toLowerCase() + "%";
            spec = spec.and((root, query, cb) -> cb.or(
                cb.like(cb.lower(root.get("name")), like),
                cb.like(root.get("phone"), "%" + search + "%")
            ));
        }

        return repo.findAll(spec,
                org.springframework.data.domain.Sort.by(
                        org.springframework.data.domain.Sort.Direction.DESC, "createdAt"));
    }

    @Override
    public Optional<Appointment> findById(UUID id) {
        return repo.findById(id);
    }

    @Override
    public Appointment updateStatus(UUID id, String status) {
        Appointment appt = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found: " + id));
        appt.setStatus(status);
        return repo.save(appt);
    }

    @Override
    public void delete(UUID id) {
        if (!repo.existsById(id)) {
            throw new ResourceNotFoundException("Appointment not found: " + id);
        }
        repo.deleteById(id);
    }
}
