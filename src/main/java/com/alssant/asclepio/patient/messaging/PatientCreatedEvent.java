package com.alssant.asclepio.patient.messaging;

import java.util.UUID;

public record PatientCreatedEvent(
        UUID patientId,
        String name
) {

}
