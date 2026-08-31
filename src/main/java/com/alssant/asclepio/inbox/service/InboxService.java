package com.alssant.asclepio.inbox.service;

import com.alssant.asclepio.inbox.domain.InboxEvent;
import com.alssant.asclepio.inbox.mapper.InboxEventMapper;
import com.alssant.asclepio.inbox.repository.InboxEventRepository;
import com.alssant.asclepio.patient.messaging.EventEnvelope;
import com.alssant.asclepio.shared.exception.InvalidEventException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class InboxService {
    private static final Logger logger = LoggerFactory.getLogger(InboxService.class);

    private final InboxEventRepository repository;
    private final InboxEventMapper mapper;

    public InboxService(InboxEventRepository repository, InboxEventMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public boolean register(EventEnvelope envelope) {
        validateStructure(envelope);
        if (isAlreadyRegistered(envelope)) {
            logger.debug(
                    "Ignoring duplicated event {}",
                    envelope.metadata().eventId());
            return false;
        }

        InboxEvent inboxEvent = mapper.toEntity(envelope);
        logger.info("Registering event [{}]", envelope.metadata().eventId());
        repository.save(inboxEvent);

        logger.info("Event persisted");
        return true;
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

    private boolean isAlreadyRegistered(EventEnvelope envelope) {
        return repository.existsById(envelope.metadata().eventId());
    }
}
