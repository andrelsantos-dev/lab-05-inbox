package com.alssant.asclepio.integration;

import com.alssant.asclepio.config.TestcontainersConfiguration;
import com.alssant.asclepio.config.rabbit.RabbitProperties;
import com.alssant.asclepio.inbox.consumer.PatientCreatedConsumer;
import com.alssant.asclepio.inbox.domain.InboxEvent;
import com.alssant.asclepio.inbox.mapper.InboxEventMapper;
import com.alssant.asclepio.inbox.repository.InboxEventRepository;
import com.alssant.asclepio.notification.repository.NotificationRepository;
import com.alssant.asclepio.notification.service.NotificationService;
import com.alssant.asclepio.patient.messaging.EventEnvelope;
import com.alssant.asclepio.support.EventFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.awaitility.Durations;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@Import(TestcontainersConfiguration.class)
@SpringBootTest
public class RabbitConsumerIntegrationTest {
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private RabbitProperties properties;

    @MockitoSpyBean
    private PatientCreatedConsumer consumer;

    @Autowired
    private InboxEventRepository inboxEventRepository;

    @Autowired
    private NotificationRepository notificationRepository;

    @MockitoSpyBean
    private NotificationService notificationService;

    @Autowired
    private InboxEventMapper mapper;

    @Test
    void shouldReceivePatientCreatedEvent() {
        EventEnvelope envelope = EventFactory.patientCreated(objectMapper);

        rabbitTemplate.convertAndSend(properties.exchange(), properties.routingKey(), envelope);

        await().atMost(Durations.FIVE_SECONDS).untilAsserted(() -> {
            ArgumentCaptor<EventEnvelope> payloadCaptor = ArgumentCaptor.forClass(EventEnvelope.class);
            verify(consumer).onReceive(payloadCaptor.capture());
            EventEnvelope consumed = payloadCaptor.getValue();
            assertThat(consumed).usingRecursiveComparison().isEqualTo(envelope);
        });
    }

    @Test
    void shouldRegisterReceivedEvent() {
        EventEnvelope envelope = EventFactory.patientCreated(objectMapper);

        rabbitTemplate.convertAndSend(properties.exchange(), properties.routingKey(), envelope);

        await().atMost(Durations.FIVE_SECONDS).untilAsserted(() -> {
            Optional<InboxEvent> optEvent =
                    inboxEventRepository.findById(envelope.metadata().eventId());

            assertThat(optEvent).isPresent();

            InboxEvent event = optEvent.orElseThrow();

            assertThat(event)
                    .usingRecursiveComparison()
                    .ignoringFields("receivedAt")
                    .isEqualTo(mapper.toEntity(envelope));
        });

    }

    @Test
    void shouldIgnoreDuplicatedEvent() {
        EventEnvelope envelope = EventFactory.patientCreated(objectMapper);

        rabbitTemplate.convertAndSend(properties.exchange(), properties.routingKey(), envelope);
        rabbitTemplate.convertAndSend(properties.exchange(), properties.routingKey(), envelope);

        await().atMost(Durations.FIVE_SECONDS).untilAsserted(() -> {
            verify(consumer, times(2)).onReceive(any());

            List<InboxEvent> events = inboxEventRepository
                    .findByAggregateId(envelope.metadata().aggregateId());
            ;

            assertThat(events).hasSize(1);

            assertThat(events.getFirst())
                    .usingRecursiveComparison()
                    .ignoringFields("receivedAt")
                    .isEqualTo(mapper.toEntity(envelope));
        });

    }

    @Test
    void shouldCreateNotificationForNewPatient() {
        EventEnvelope envelope = EventFactory.patientCreated(objectMapper);

        rabbitTemplate.convertAndSend(properties.exchange(), properties.routingKey(), envelope);

        await().atMost(Durations.FIVE_SECONDS).untilAsserted(() -> {
            assertThat(inboxEventRepository.findById(envelope.metadata().eventId()))
                    .isPresent();

            assertThat(notificationRepository.findByPatientId(envelope.metadata().aggregateId()))
                    .isPresent();
        });

    }

}
