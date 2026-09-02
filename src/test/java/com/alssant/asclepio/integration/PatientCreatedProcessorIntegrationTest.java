package com.alssant.asclepio.integration;

import com.alssant.asclepio.config.TestcontainersConfiguration;
import com.alssant.asclepio.inbox.repository.InboxEventRepository;
import com.alssant.asclepio.inbox.service.PatientCreatedProcessor;
import com.alssant.asclepio.notification.repository.NotificationRepository;
import com.alssant.asclepio.notification.service.NotificationService;
import com.alssant.asclepio.patient.messaging.EventEnvelope;
import com.alssant.asclepio.support.EventFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;

@Import(TestcontainersConfiguration.class)
@SpringBootTest
public class PatientCreatedProcessorIntegrationTest {
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private InboxEventRepository inboxEventRepository;

    @Autowired
    private NotificationRepository notificationRepository;

    @MockitoSpyBean
    private NotificationService notificationService;

    @Autowired
    private PatientCreatedProcessor patientCreatedProcessor;

    @Test
    void shouldRollbackWhenNotificationCreationFails() {
        EventEnvelope envelope = EventFactory.patientCreated(objectMapper);
        doThrow(new RuntimeException("Notification Failed")).when(notificationService).create(any());

        assertThatThrownBy(() -> patientCreatedProcessor.process(envelope))
                .isInstanceOf(RuntimeException.class);

        assertThat(inboxEventRepository.findById(envelope.metadata().eventId()))
                .isNotPresent();

        assertThat(notificationRepository.findByPatientId(envelope.metadata().aggregateId()))
                .isNotPresent();
    }
}
