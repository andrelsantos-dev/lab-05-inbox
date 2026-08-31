package com.alssant.asclepio.notification;

import com.alssant.asclepio.patient.messaging.PatientCreatedEvent;

@FunctionalInterface
public interface NotificationService {
    void create(PatientCreatedEvent event);
}
