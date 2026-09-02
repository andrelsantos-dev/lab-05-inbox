package com.alssant.asclepio.notification.service;

import com.alssant.asclepio.notification.domain.Notification;
import com.alssant.asclepio.notification.repository.NotificationRepository;
import com.alssant.asclepio.patient.messaging.PatientCreatedEvent;
import org.springframework.stereotype.Service;

@Service
public class NotificationPersistenceService implements NotificationService {
    private final NotificationRepository repository;

    public NotificationPersistenceService(NotificationRepository repository) {
        this.repository = repository;
    }

    @Override
    public void create(PatientCreatedEvent event) {
        Notification notification = new Notification();
        notification.setPatientId(event.patientId());
        notification.setPatientName(event.name());

        repository.save(notification);

    }
}
