## Phase 03 — Registering Received Events

### Objective

The goal of this phase is to persist every received event before introducing idempotency.

At this point, the application still does **not** decide whether an event should be processed. Its only responsibility is to register that the event was successfully received.

This creates the foundation required for the Inbox Pattern.

---

## InboxEvent

An `InboxEvent` entity was introduced to represent every event received by the consumer.

Unlike the Outbox Pattern, the Inbox does not manage publication attempts or retries.

Its responsibility is much simpler:

* Store the event identifier.
* Store the event metadata.
* Store the original payload.
* Record when the event was received.

```text
EventEnvelope
        │
        ▼
InboxEvent
```

The persisted entity is almost a direct representation of the incoming `EventEnvelope`.

---

## InboxEventMapper

A dedicated mapper converts the messaging contract into the persistence model.

```text
EventEnvelope
        │
        ▼
InboxEventMapper
        │
        ▼
InboxEvent
```

Keeping this transformation isolated provides several advantages:

* separates messaging concerns from persistence;
* keeps the consumer simple;
* centralizes mapping logic in a single component;
* makes the transformation independently testable.

---

## InboxService

The `InboxService` introduces the first application use case.

```mermaid
flowchart TD
%% Style and Color Configurations
    classDef default fill:#F8FAFC,stroke:#64748B,stroke-width:1px,color:#0F172A,font-family:Helvetica;
    classDef boundary fill:#F1F5F9,stroke:#CBD5E1,stroke-width:1px,stroke-dasharray: 5 5,color:#475569,font-weight:bold;
    classDef entry fill:#3B82F6,stroke:#1D4ED8,stroke-width:2px,color:#FFFFFF,font-weight:bold;
    classDef domain fill:#F1F5F9,stroke:#0F172A,stroke-width:2px,color:#0F172A;
    classDef success fill:#10B981,stroke:#047857,stroke-width:2px,color:#FFFFFF,font-weight:bold;

%% Bounded Contexts / Subgraphs
    subgraph SERVICE [Application Service / Use Case]
        A([register])
        B[validate]
    end

    subgraph DOMAIN [Domain Model]
        C[[toEntity]]
    end

    subgraph INFRA [Infrastructure / Persistence]
        D[(save)]
    end

%% Data Flow
    A --> B
    B -->|DTO Validated| C
    C -->|Domain Entity| D

%% Class Assignment
    class SERVICE,DOMAIN,INFRA boundary;
    class A entry;
    class C domain;
    class D success;
```

Although the current implementation only performs structural validation, the service was intentionally designed to evolve.

Future phases will extend the validation step to include duplicate detection before persisting the event.

---

## Validation

The first validation layer verifies the integrity of the incoming event.

Current checks include:

* Event envelope
* Event metadata
* Event payload

At this stage, no duplicate verification is performed.

That responsibility belongs to the next phase.

---

## Testing Strategy

Two complementary testing approaches are now in place.

### Unit Test

`InboxEventMapperTest`

Validates the mapping between `EventEnvelope` and `InboxEvent`.

The test verifies every mapped field individually, ensuring the mapper correctly transfers all event information while leaving infrastructure-managed fields (such as `receivedAt`) untouched.

---

### Integration Test

`RabbitConsumerIntegrationTest`

The integration test validates the complete processing pipeline.

```mermaid
flowchart TD
%% Style and Color Configurations
    classDef default fill:#F8FAFC,stroke:#64748B,stroke-width:1px,color:#0F172A,font-family:Helvetica;
    classDef boundary fill:#F1F5F9,stroke:#CBD5E1,stroke-width:1px,stroke-dasharray: 5 5,color:#475569,font-weight:bold;
    classDef entry fill:#3B82F6,stroke:#1D4ED8,stroke-width:2px,color:#FFFFFF,font-weight:bold;
    classDef success fill:#10B981,stroke:#047857,stroke-width:2px,color:#FFFFFF,font-weight:bold;
    classDef Broker fill:#FFF7ED,stroke:#EA580C,stroke-width:1px,color:#7C2D12;

%% Bounded Contexts / Subgraphs
    subgraph PRODUCER [Producer Application]
        A([RabbitTemplate])
    end

    subgraph RMQ [Message Broker - RabbitMQ]
        B[(RabbitMQ Queue)]
    end

subgraph CONSUMER [Consumer Application]
C[[RabbitListener]]
D[InboxService]
end

subgraph DB [Database - Idempotent Inbox]
E[(InboxRepository)]
end

%% Data Flow
A -->|Send Message| B
B -->|Listen / Consume| C
C --> D
D -->|Save / Process| E

%% Class Assignment
class PRODUCER,RMQ,CONSUMER,DB boundary;
class A entry;
class E success;
class B Broker;
```

Instead of verifying only that the consumer method was invoked, the test now validates the observable result: the received event is successfully persisted in the Inbox.

---

## Architectural Note

This phase intentionally stops after persisting the received event.

The application **does not yet** answer the question:

> "Has this event already been processed?"

Instead, it prepares the necessary data to answer that question in the next phase.

This incremental approach keeps each phase focused on a single responsibility and mirrors the natural evolution of the Inbox Pattern implementation.
