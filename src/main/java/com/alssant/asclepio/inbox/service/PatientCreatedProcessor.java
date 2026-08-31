package com.alssant.asclepio.inbox.service;

import com.alssant.asclepio.patient.messaging.EventEnvelope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PatientCreatedProcessor {
    private final InboxService inboxService;

    public PatientCreatedProcessor(InboxService inboxService) {
        this.inboxService = inboxService;
    }

    @Transactional
    public void process(EventEnvelope envelope) {
        boolean registered = this.inboxService.register(envelope);
        if (!registered) {
            return;
        }

        //TODO: process notification
    }
}
