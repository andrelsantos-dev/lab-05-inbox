package com.alssant.asclepio.support;

import com.alssant.asclepio.patient.messaging.EventEnvelope;
import com.alssant.asclepio.patient.messaging.EventMetadata;
import com.alssant.asclepio.patient.messaging.EventType;
import com.alssant.asclepio.patient.messaging.PatientCreatedEvent;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.UUID;

public class EventFactory {
    public static EventEnvelope patientCreated(
            ObjectMapper mapper,
            UUID eventId,
            UUID tenantId,
            UUID patientId,
            String patientName) {

        EventMetadata metadata = new EventMetadata(
                eventId,
                EventType.PATIENT_CREATED,
                patientId,
                "Patient",
                tenantId
        );

        JsonNode payload = mapper.valueToTree(
                new PatientCreatedEvent(patientId, patientName)
        );

        return new EventEnvelope(metadata, payload);
    }

    public static EventEnvelope patientCreated(ObjectMapper mapper) {
        return patientCreated(
                mapper,
                UUID.randomUUID(),
                UUID.randomUUID(),
                UUID.randomUUID(),
                "PATIENT-" + UUID.randomUUID()
        );
    }
}
