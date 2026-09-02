package com.alssant.asclepio.inbox.service;

import com.alssant.asclepio.notification.service.NotificationService;
import com.alssant.asclepio.patient.mapper.PatientCreatedEventMapper;
import com.alssant.asclepio.patient.messaging.EventEnvelope;
import com.alssant.asclepio.patient.messaging.PatientCreatedEvent;
import com.alssant.asclepio.support.EventFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class PatientCreatedProcessorTest {
    @Mock
    private InboxService inboxService;
    @Mock
    private PatientCreatedEventMapper patientCreatedEventMapper;
    @Mock
    private NotificationService notificationService;
    @InjectMocks
    private ObjectMapper objectMapper;
    @InjectMocks
    private PatientCreatedProcessor patientCreatedProcessor;


    @Test
    void shouldNotifyOnNewEvent() {
        EventEnvelope envelope = EventFactory.patientCreated(objectMapper);
        PatientCreatedEvent mappedEvent = new PatientCreatedEvent(UUID.randomUUID(), "NAME");

        //given
        Mockito
                .when(inboxService.register(Mockito.same(envelope)))
                .thenReturn(true);
        Mockito.when(patientCreatedEventMapper.toEvent(envelope)).thenReturn(mappedEvent);

        //when
        patientCreatedProcessor.process(envelope);

        //then
        Mockito.verify(notificationService, Mockito.times(1)).create(Mockito.same(mappedEvent));
    }

    @Test
    void shouldNotNotifyOnAlreadyProcessedEvent() {
        EventEnvelope envelope = EventFactory.patientCreated(objectMapper);

        //given
        Mockito
                .when(inboxService.register(Mockito.same(envelope)))
                .thenReturn(false);

        //when
        patientCreatedProcessor.process(envelope);

        //then
        Mockito.verify(patientCreatedEventMapper, Mockito.never()).toEvent(any());
        Mockito.verify(notificationService, Mockito.never()).create(any());
    }
}