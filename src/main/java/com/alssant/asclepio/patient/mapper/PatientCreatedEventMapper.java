package com.alssant.asclepio.patient.mapper;

import com.alssant.asclepio.patient.messaging.EventEnvelope;
import com.alssant.asclepio.patient.messaging.PatientCreatedEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

@Component
public class PatientCreatedEventMapper {
    private final ObjectMapper objectMapper;

    public PatientCreatedEventMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public PatientCreatedEvent toEvent(EventEnvelope envelope) {
        return objectMapper.convertValue(envelope.payload(), PatientCreatedEvent.class);
    }
}
