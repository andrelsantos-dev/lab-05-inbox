package com.alssant.asclepio.patient.dto;

import jakarta.validation.constraints.NotBlank;

public record CreatePatientRequest(@NotBlank String name) {
}
