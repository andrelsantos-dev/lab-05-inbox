## Phase 02 — RabbitMQ Consumer

### Objective

In this phase, the application becomes an **event consumer**.

The goal is to establish the messaging infrastructure required to receive events published by external services. At this stage, no business processing or Inbox logic is implemented yet.

The focus is to understand how Spring AMQP delivers messages from RabbitMQ to the application.

---

## What was implemented

### RabbitMQ Infrastructure

The messaging infrastructure was configured using Spring AMQP:

* Topic Exchange
* Queue
* Binding
* JSON message conversion using `Jackson2JsonMessageConverter`

Application settings are externalized through `RabbitProperties`, keeping the messaging configuration clean and easy to maintain.

---

### Event Contract

The consumer receives events wrapped by an `EventEnvelope`.

The envelope separates transport metadata from the business payload.

```
EventEnvelope
├── EventMetadata
└── Payload (PatientCreatedEvent)
```

The metadata includes information required by distributed systems, such as:

* Event identifier
* Event type
* Aggregate identifier
* Aggregate type
* Tenant identifier

The payload contains the business data associated with the event.

---

### Rabbit Listener

A `@RabbitListener` was introduced to receive messages published to the configured queue.

At this stage, the listener intentionally performs no business logic.

Its only responsibility is to demonstrate that the messaging infrastructure is correctly configured and that messages are successfully delivered to the application.

---

### Integration Test

An integration test validates the complete messaging flow.

```
RabbitTemplate
        │
        ▼
RabbitMQ Exchange
        │
        ▼
Queue
        │
        ▼
@RabbitListener
```

The test publishes a real message using `RabbitTemplate` and verifies that the listener receives the expected `EventEnvelope`.

This confirms:

* RabbitMQ connectivity
* Exchange and Queue configuration
* Routing
* JSON serialization/deserialization
* Spring AMQP listener configuration

---

### Architectural Note — Consumers Own Their Own Data

Initially, this lab reused the `Patient` entity from the previous Outbox lab.

During the design process, it became clear that this would not accurately represent an event-driven architecture.

Although both labs work with the same business concept (**Patient**), they represent different services.

* **Lab 04** is responsible for creating patients and publishing events.
* **Lab 05** is responsible only for consuming those events.

For this reason, this project keeps only the event contract (`patient.messaging`) and does not persist `Patient` entities.

This reflects an important architectural principle:

> Services communicate through events, not by sharing domain models.

The consumer owns its own persistence model, which will be introduced in later phases.

---

## Current Flow

```
External Producer
        │
        ▼
RabbitMQ
        │
        ▼
PatientCreatedConsumer
```

At this point, the application is capable of consuming events but does not yet persist them or execute business logic.

Those responsibilities will be introduced incrementally in the next phases.
