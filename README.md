# Legal Metrology Verification System

A Spring Boot backend application for managing the verification and scheduling of weighing and measuring instruments under Legal Metrology.

## 📌 Project Overview

The Legal Metrology Verification System is designed to digitize the process of instrument verification and certification.

The system provides APIs for:

- Managing verification schedules
- Storing schedule information
- Checking measured values against permissible limits
- Automatically determining whether an instrument passes or fails verification
- Connecting the application to a PostgreSQL database

The project is being developed as a backend prototype that can later be connected to a web or mobile frontend.

---

## 🚀 Technologies Used

- **Java**
- **Spring Boot**
- **Spring Data JPA**
- **Hibernate**
- **PostgreSQL**
- **Maven**
- **REST APIs**
- **Git & GitHub**

---

## 📂 Project Structure

```text
legal-meterology/
│
├── src/
│   └── main/
│       ├── java/
│       │   └── com/example/legal_meterology/
│       │       │
│       │       ├── controller/
│       │       │   ├── ScheduleController.java
│       │       │   └── VerificationController.java
│       │       │
│       │       ├── entity/
│       │       │   └── Schedule.java
│       │       │
│       │       ├── repository/
│       │       │   └── ScheduleRepository.java
│       │       │
│       │       └── Service/
│       │           └── VerificationService.java
│       │
│       └── resources/
│           └── application.properties
│
├── pom.xml
├── mvnw
└── mvnw.cmd
