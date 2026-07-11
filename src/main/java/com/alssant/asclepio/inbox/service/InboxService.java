package com.alssant.asclepio.inbox.service;

import com.alssant.asclepio.inbox.domain.InboxEvent;
import com.alssant.asclepio.inbox.mapper.InboxEventMapper;
import com.alssant.asclepio.inbox.repository.InboxEventRepository;
import com.alssant.asclepio.patient.messaging.EventEnvelope;
import com.alssant.asclepio.shared.exception.InvalidEventException;
import org.springframework.stereotype.Service;

@Service
public class InboxService {
    private final InboxEventRepository repository;
    private final InboxEventMapper mapper;

    public InboxService(InboxEventRepository repository, InboxEventMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public void register(EventEnvelope envelope) {
        validate(envelope);
        InboxEvent inboxEvent = mapper.toEntity(envelope);

        repository.save(inboxEvent);
    }

    private void validate(EventEnvelope envelope) {
        this.validateStructure(envelope);
    }

    private void validateStructure(EventEnvelope envelope) {
        if (envelope == null) {
            throw new InvalidEventException("Event envelope must not be null");
        }

        if (envelope.metadata() == null) {
            throw new InvalidEventException("Event metadata must not be null");
        }

        if (envelope.metadata().eventId() == null) {
            throw new InvalidEventException("Event id must not be null");
        }

        if (envelope.payload() == null) {
            throw new InvalidEventException("Event payload must not be null");
        }
    }
}
