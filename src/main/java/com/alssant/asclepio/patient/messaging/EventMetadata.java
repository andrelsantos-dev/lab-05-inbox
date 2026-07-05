package com.alssant.asclepio.patient.messaging;

import java.util.UUID;

public record EventMetadata(
        UUID eventId,
        EventType eventType,
        UUID aggregateId,
        String aggregateType,
        UUID tenantId
) {
}
