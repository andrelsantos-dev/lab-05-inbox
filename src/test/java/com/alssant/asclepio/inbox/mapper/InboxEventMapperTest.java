package com.alssant.asclepio.inbox.mapper;

import com.alssant.asclepio.inbox.domain.InboxEvent;
import com.alssant.asclepio.patient.messaging.EventEnvelope;
import com.alssant.asclepio.support.EventFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;


class InboxEventMapperTest {
    private final InboxEventMapper mapper = new InboxEventMapper();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void shouldMapEnvelopeToInboxEvent() {
        EventEnvelope envelope = EventFactory.patientCreated(objectMapper);
        InboxEvent inboxEvent = mapper.toEntity(envelope);

        assertThat(inboxEvent).isNotNull();
        assertThat(inboxEvent.getEventType()).isEqualTo(envelope.metadata().eventType());
        assertThat(inboxEvent.getTenantId()).isEqualTo(envelope.metadata().tenantId());
        assertThat(inboxEvent.getEventId()).isEqualTo(envelope.metadata().eventId());
        assertThat(inboxEvent.getAggregateId()).isEqualTo(envelope.metadata().aggregateId());
        assertThat(inboxEvent.getAggregateType()).isEqualTo(envelope.metadata().aggregateType());
        assertThat(inboxEvent.getPayload()).isEqualTo(envelope.payload());
        assertThat(inboxEvent.getReceivedAt()).isNull();
    }
}