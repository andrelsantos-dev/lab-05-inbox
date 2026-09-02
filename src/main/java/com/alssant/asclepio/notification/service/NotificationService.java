package com.alssant.asclepio.notification.service;

import com.alssant.asclepio.patient.messaging.PatientCreatedEvent;

@FunctionalInterface
public interface NotificationService {
    void create(PatientCreatedEvent event);
}
