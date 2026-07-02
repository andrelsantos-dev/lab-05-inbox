package com.alssant.asclepio.patient.service;

import com.alssant.asclepio.patient.domain.Patient;
import com.alssant.asclepio.patient.dto.CreatePatientRequest;
import com.alssant.asclepio.patient.dto.PatientResponse;
import com.alssant.asclepio.patient.repository.PatientRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.UUID;

@Service
public class PatientService {
    private final PatientRepository repository;

    public PatientService(PatientRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<PatientResponse> findAll() {
        return repository
                .findAll()
                .stream()
                .map(
                        patient -> new PatientResponse(
                                patient.getId(),
                                patient.getTenantId(),
                                patient.getName()
                        )).toList();
    }

    @Transactional
    public PatientResponse create(CreatePatientRequest request, UUID tenantId) {
        if (tenantId == null) {
            throw new IllegalStateException("Tenant not set");
        }

        if (!StringUtils.hasText(request.name())) {
            throw new IllegalStateException("Name not set");
        }

        Patient p = repository.save(
                new Patient(request.name(),
                        tenantId));

        return new PatientResponse(
                p.getId(),
                p.getTenantId(),
                p.getName()
        );
    }
}
