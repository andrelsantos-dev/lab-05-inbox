package com.alssant.asclepio.patient.messaging;

import com.fasterxml.jackson.databind.JsonNode;

public record EventEnvelope(
        EventMetadata metadata,
        JsonNode payload
) {
}
