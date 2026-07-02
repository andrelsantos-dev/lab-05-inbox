# Lab 05 — Inbox Pattern & Idempotent Consumer (Asclépio)

![Java](https://img.shields.io/badge/Java-21-ED8B00?style=for-the-badge&logo=openjdk)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.x-6DB33F?style=for-the-badge&logo=springboot)
![Gradle](https://img.shields.io/badge/Gradle-Kotlin_DSL-02303A?style=for-the-badge&logo=gradle)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-17-4169E1?style=for-the-badge&logo=postgresql)
![RabbitMQ](https://img.shields.io/badge/RabbitMQ-4.x-FF6600?style=for-the-badge&logo=rabbitmq)

## Overview

This lab demonstrates the **Inbox Pattern**, a common solution used in event-driven architectures to guarantee **idempotent message processing**.

It is a continuation of **Lab 04 – Transactional Outbox**, completing the reliable event delivery flow by focusing on the consumer side.

The project uses a simplified healthcare domain (**Asclépio**) where patient-related events will be consumed safely, ensuring that duplicate message deliveries do not result in duplicated business operations.

> **Current Status:** Bootstrap completed. The Inbox Pattern implementation will be developed incrementally throughout the following phases.
> 
> **Note:** Starting with this lab, the project adopts Gradle Kotlin DSL, feature-based package organization, and Spring Boot's @ServiceConnection support to provide a cleaner and more modern development experience.

---

## Learning Objectives

- Understand the Inbox Pattern.
- Learn why message duplication occurs in distributed systems.
- Implement idempotent consumers.
- Persist processed events for deduplication.
- Explore reliable event processing using RabbitMQ.
- Practice integration testing using Testcontainers.

---

## Technology Stack

- Java 21
- Spring Boot 3
- Gradle Kotlin DSL
- Spring Web
- Spring Data JPA
- Bean Validation
- RabbitMQ
- PostgreSQL
- Flyway
- Testcontainers

---

## Project Structure

```
src/main/java/com/alssant/asclepio
├── config
├── inbox
│   ├── domain
│   ├── dto
│   ├── repository
│   └── service
├── patient
│   ├── controller
│   ├── domain
│   ├── dto
│   ├── repository
│   └── service
└── shared
    └── exception
```

The project is organized by **feature**, keeping each business module self-contained instead of grouping classes by technical layer.

---

## Current Phase
* Phase 01 — Bootstrap
  * Gradle Kotlin DSL
  * PostgreSQL integration
  * Flyway migrations
  * Testcontainers with `@ServiceConnection`
  * Basic Patient API


---

## Relationship with Previous Labs

| Lab | Focus |
|------|-------|
| Lab 01 | Flyway + Testcontainers |
| Lab 02 | Tenant Context |
| Lab 03 | PostgreSQL Row-Level Security |
| Lab 04 | Transactional Outbox |
| **Lab 05** | Inbox Pattern & Idempotent Consumer |

Together, Lab 04 and Lab 05 demonstrate both sides of reliable event-driven communication:

- **Outbox** guarantees reliable event publication.
- **Inbox** guarantees reliable and idempotent event consumption.

---

## Next Steps

The next phases will progressively introduce:

- RabbitMQ messaging
- Event consumers
- Inbox persistence
- Duplicate message detection
- Idempotent processing
- Integration tests for at-least-once delivery scenarios