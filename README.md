# Order Service API

[![Java 21](https://img.shields.io/badge/Java-21-ED8B00.svg?logo=openjdk&logoColor=white)](#prerequisites--runtime)
[![Spring Boot 4](https://img.shields.io/badge/Spring%20Boot-4.1.2-6DB33F.svg?logo=springboot&logoColor=white)](#prerequisites--runtime)
[![Build Tool](https://img.shields.io/badge/Build-Maven%20Wrapper-C71A36.svg?logo=apachemaven&logoColor=white)](#local-development--testing)

A lightweight enterprise REST microservice for order ingestion and lifecycle state management, pre-instrumented with Spring Boot Actuator probes for container health checks, platform telemetry, and orchestration readiness.

---

## Architecture & Code Conventions

* **Spring Boot 4 & Java 21:** Implemented using modern Java features (immutable Records for DTOs, clean REST controllers).
* **Decoupled Platform Delivery:** Business logic and automated test suites live strictly inside this repository; containerization, vulnerability scanning, and multienvironment delivery pipelines are orchestrated externally via `platform-delivery-engine`.
* **Telemetry & Probes:** Spring Boot Actuator exposes health indicators consumed by Docker Compose and Kubernetes orchestration layers.

---

## Endpoints Specification

| Method | Endpoint           | Description                        | Expected Status |
|:-------|:-------------------|:-----------------------------------|:----------------|
| `GET`  | `/api/v1/orders`   | Retrieve list of registered orders | `200 OK`        |
| `POST` | `/api/v1/orders`   | Submit and validate a new order    | `201 Created`   |
| `GET`  | `/actuator/health` | Liveness & Readiness health probe  | `200 UP`        |
| `GET`  | `/actuator/info`   | Application metadata               | `200 OK`        |

---

## Prerequisites & Runtime

* **Java Development Kit (JDK):** Version 21 (e.g., Amazon Corretto 21 or Eclipse Temurin 21)
* **Build System:** Apache Maven (pre-configured via included `./mvnw` wrapper)

---

## Local Development & Testing

Run all commands from the project root using the Maven Wrapper:

```bash
# 1. Run unit and integration tests
./mvnw clean test

# 2. Package executable JAR (skipped during fast local runs)
./mvnw clean package

# 3. Start the application locally on port 8080
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
│   │   ├── java/              # Domain logic, controllers, and services
│   │   └── resources/         # application.properties / yaml
│   └── test/java/             # Unit and integration test suites
├── mvnw                       # Linux/macOS wrapper script
├── mvnw.cmd                   # Windows wrapper script
├── pom.xml                    # Project dependencies & build lifecycle
└── README.md

```

---

## Author

**Bruno Leal**

*Software & Platform Tooling Engineer*

[LinkedIn](https://linkedin.com/in/bruno-m-d-leal) • [GitHub](https://github.com/brunomdleal)