package com.alssant.asclepio.inbox.service;

import com.alssant.asclepio.notification.service.NotificationService;
import com.alssant.asclepio.patient.mapper.PatientCreatedEventMapper;
import com.alssant.asclepio.patient.messaging.EventEnvelope;
import com.alssant.asclepio.patient.messaging.PatientCreatedEvent;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PatientCreatedProcessor {
    private final InboxService inboxService;
    private final PatientCreatedEventMapper patientCreatedEventMapper;
    private final NotificationService notificationService;


    public PatientCreatedProcessor(InboxService inboxService, PatientCreatedEventMapper patientCreatedEventMapper, NotificationService notificationService) {
        this.inboxService = inboxService;
        this.patientCreatedEventMapper = patientCreatedEventMapper;
        this.notificationService = notificationService;
    }

    @Transactional
    public void process(EventEnvelope envelope) {
        boolean registered = this.inboxService.register(envelope);
        if (!registered) {
            return;
        }

        PatientCreatedEvent event = patientCreatedEventMapper.toEvent(envelope);
        notificationService.create(event);
    }
}
