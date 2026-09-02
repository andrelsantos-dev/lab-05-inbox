CREATE TABLE notifications
(
    notification_id       UUID PRIMARY KEY,
    patient_id      UUID         NOT NULL,
    patient_name     VARCHAR(255) NOT NULL,
    created_at    TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP
);