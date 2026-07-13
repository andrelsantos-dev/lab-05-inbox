package com.alssant.asclepio.inbox.service;

import com.alssant.asclepio.inbox.domain.InboxEvent;
import com.alssant.asclepio.inbox.mapper.InboxEventMapper;
import com.alssant.asclepio.inbox.repository.InboxEventRepository;
import com.alssant.asclepio.patient.messaging.EventEnvelope;
import com.alssant.asclepio.shared.exception.InvalidEventException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class InboxService {
    private static final Logger logger = LoggerFactory.getLogger(InboxService.class);

    private final InboxEventRepository repository;
    private final InboxEventMapper mapper;

    public InboxService(InboxEventRepository repository, InboxEventMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Transactional
    public void register(EventEnvelope envelope) {
        validateStructure(envelope);
        if(isAlreadyRegistered(envelope)) {
            logger.debug(
                    "Ignoring duplicated event {}",
                    envelope.metadata().eventId());
            return;
        }

        InboxEvent inboxEvent = mapper.toEntity(envelope);
        logger.info("Registering event [{}]", envelope.metadata().eventId());

        try {
            repository.save(inboxEvent);
        }catch (Throwable e) {
            logger.error("Failed to register event [{}]", envelope.metadata().eventId(), e);
        }


        logger.info("Event persisted");
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
