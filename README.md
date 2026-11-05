# Order Service API

[![Java 21](https://img.shields.io/badge/Java-21-ED8B00.svg?logo=openjdk&logoColor=white)](#prerequisites--runtime)
[![Spring Boot 3.4](https://img.shields.io/badge/Spring%20Boot-3.4.x-6DB33F.svg?logo=springboot&logoColor=white)](#prerequisites--runtime)
[![PostgreSQL](https://img.shields.io/badge/Database-PostgreSQL%2016-4169E1.svg?logo=postgresql&logoColor=white)](#database-migration--persistence)
[![Flyway](https://img.shields.io/badge/Migrations-Flyway-CC0200.svg?logo=flyway&logoColor=white)](#database-migration--persistence)
[![Build Tool](https://img.shields.io/badge/Build-Maven%20Wrapper-C71A36.svg?logo=apachemaven&logoColor=white)](#local-development--testing)

A lightweight enterprise REST microservice for order ingestion and lifecycle state management. Engineered with Spring Data JPA, automated schema migrations via Flyway, and pre-instrumented with Spring Boot Actuator probes for container health checks, platform telemetry, and orchestration readiness.

---

## Architecture & Code Conventions

- **Spring Boot 3.4 & Java 21:** Implemented using modern Java features (immutable Records for request/response DTOs, clean layered REST controllers, service layer, and JPA repositories).
- **Relational Persistence & Migration:** Production-ready PostgreSQL storage with declarative Flyway database schema versioning executing during startup.
- **Decoupled Platform Delivery:** Business logic, schema migrations, and automated test suites live strictly inside this repository; containerization, security audits, and multi-environment orchestration are managed externally via `platform-delivery-engine`.
- **Telemetry & Probes:** Spring Boot Actuator exposes health indicators (`/actuator/health`) consumed by Docker Compose and Kubernetes orchestration layers for liveness and readiness tracking.

---

## Database Migration & Persistence

Database changes are managed as versioned migration scripts under `src/main/resources/db/migration/`:

```text
src/main/resources/db/migration/
└── V1__create_orders_table.sql    # DDL: Base orders schema, constraints, and audit columns
```

- In production/containerized environments, datasource credentials are supplied via environment variables (`SPRING_DATASOURCE_URL`, `SPRING_DATASOURCE_USERNAME`, `SPRING_DATASOURCE_PASSWORD`).
- In automated test workflows (`mvn test`), embedded in-memory database configurations or mock providers allow unit and integration validation without requiring an active external database.

---

## Endpoints Specification

| Method | Endpoint           | Description                               | Expected Status |
|:-------|:-------------------|:------------------------------------------|:----------------|
| `GET`  | `/api/v1/orders`   | Retrieve list of registered orders        | `200 OK`        |
| `POST` | `/api/v1/orders`   | Submit, validate, and persist a new order | `201 Created`   |
| `GET`  | `/actuator/health` | Liveness & Readiness health probe         | `200 UP`        |
| `GET`  | `/actuator/info`   | Application metadata                      | `200 OK`        |

---

## Prerequisites & Runtime

- **Java Development Kit (JDK):** Version 21 (e.g., Eclipse Temurin 21 or Amazon Corretto 21)
- **Database:** PostgreSQL 16 (or local Docker container / embedded H2 for isolated unit runs)
- **Build System:** Apache Maven (pre-configured via included `./mvnw` wrapper)

---

## Local Development & Testing

Run all commands from the project root using the Maven Wrapper:

```bash
# 1. Run unit and integration tests (including Flyway validation)
./mvnw clean test

# 2. Package executable JAR
./mvnw clean package

# 3. Start the application locally
# Requires local PostgreSQL or active environment variables (e.g. from generated .env)
./mvnw spring-boot:run
```

Once running, verify that the health probe responds:

```bash
curl -i http://localhost:8080/actuator/health
```

---

## Repository Structure

```text
order-service/
├── .mvn/wrapper/              # Maven Wrapper version lock & binaries
├── src/
│   ├── main/
│   │   ├── java/              # Controller, Domain entities, DTOs, Repositories, Services
│   │   └── resources/
│   │       ├── application.properties
│   │       └── db/migration/  # Flyway versioned SQL migration scripts (V1__...)
│   └── test/java/             # Unit and integration test suites
├── mvnw                       # Linux/macOS wrapper script
├── mvnw.cmd                   # Windows wrapper script
├── pom.xml                    # Project dependencies (Spring Data JPA, PostgreSQL, Flyway)
└── README.md
```

---

## Author

**Bruno Leal**

*Software & Platform Tooling Engineer*

[LinkedIn](https://linkedin.com/in/bruno-m-d-leal) • [GitHub](https://github.com/brunomdleal)