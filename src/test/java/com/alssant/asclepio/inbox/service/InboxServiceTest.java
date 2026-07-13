package com.alssant.asclepio.inbox.service;

import com.alssant.asclepio.inbox.domain.InboxEvent;
import com.alssant.asclepio.inbox.mapper.InboxEventMapper;
import com.alssant.asclepio.inbox.repository.InboxEventRepository;
import com.alssant.asclepio.patient.messaging.EventEnvelope;
import com.alssant.asclepio.support.EventFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InboxServiceTest {
    private final ObjectMapper objectMapper = new ObjectMapper();
    @Mock
    private InboxEventRepository repository;
    @Mock
    private InboxEventMapper mapper;
    @InjectMocks
    private InboxService inboxService;

    @Test
    void shouldNotRegisterDuplicatedEvent() {
        EventEnvelope envelope = EventFactory.patientCreated(objectMapper);

        when(repository.existsById(eq(envelope.metadata().eventId())))
                .thenReturn(true);

        inboxService.register(envelope);

        verify(repository, never()).save(any());

    }

    @Test
    void shouldRegisterEventWhenItDoesNotExist() {
        EventEnvelope envelope = EventFactory.patientCreated(objectMapper);
        InboxEvent inboxEvent = new InboxEvent();

        when(repository.existsById(eq(envelope.metadata().eventId())))
                .thenReturn(false);
        when(mapper.toEntity(envelope)).thenReturn(inboxEvent);

        inboxService.register(envelope);

        verify(repository).save(inboxEvent);

    }

}