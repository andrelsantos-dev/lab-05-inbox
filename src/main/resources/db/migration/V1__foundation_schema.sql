CREATE TABLE inbox_events
(
    event_id       UUID PRIMARY KEY,
    tenant_id      UUID         NOT NULL,
    aggregate_type VARCHAR(100) NOT NULL,
    aggregate_id   UUID         NOT NULL,
    event_type     VARCHAR(255) NOT NULL,
    payload        JSONB        NOT NULL,
    received_at    TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP
);