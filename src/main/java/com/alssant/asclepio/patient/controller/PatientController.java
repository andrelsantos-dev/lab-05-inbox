package com.alssant.asclepio.patient.controller;

import com.alssant.asclepio.patient.dto.CreatePatientRequest;
import com.alssant.asclepio.patient.dto.PatientResponse;
import com.alssant.asclepio.patient.service.PatientService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@Validated
@RequestMapping("/patients")
public class PatientController {
    private final PatientService patientService;

    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    @GetMapping()
    public ResponseEntity<List<PatientResponse>> getPatients() {
        return ResponseEntity.ok(patientService.findAll());
    }

    @PostMapping()
    public ResponseEntity<PatientResponse> createPatient(
            @RequestBody @Valid CreatePatientRequest request,
            @RequestHeader("X-Tenant-Id") @NotNull UUID tenantId) {
        PatientResponse response = patientService.create(request, tenantId);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);

    }


}
