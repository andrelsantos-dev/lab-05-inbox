package com.alssant.asclepio.inbox.mapper;

import com.alssant.asclepio.inbox.domain.InboxEvent;
import com.alssant.asclepio.patient.messaging.EventEnvelope;
import org.springframework.stereotype.Component;

@Component
public class InboxEventMapper {

    public InboxEvent toEntity(EventEnvelope envelope) {

        InboxEvent inboxEvent = new InboxEvent();
        inboxEvent.setEventId(envelope.metadata().eventId());
        inboxEvent.setTenantId(envelope.metadata().tenantId());
        inboxEvent.setEventType(envelope.metadata().eventType());
        inboxEvent.setAggregateId(envelope.metadata().aggregateId());
        inboxEvent.setAggregateType(envelope.metadata().aggregateType());
        inboxEvent.setPayload(envelope.payload());

        return inboxEvent;
    }

}
