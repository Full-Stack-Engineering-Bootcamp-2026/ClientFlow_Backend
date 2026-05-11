# ClinicFlow Backend

<div align="center">

### Spring Boot Backend for the ClinicFlow Healthcare Workflow System

Built with Java, Spring Boot, PostgreSQL, JWT Authentication, and Role-Based Access Control.

</div>

---

## About

ClinicFlow Backend is the server-side application powering the ClinicFlow healthcare workflow management system.

It handles:

* Authentication and authorization
* Role-based access control
* Staff and doctor management
* Doctor schedules and leave handling
* Patient registration
* Appointment booking and queue management
* Consultation and prescription workflows
* Patient history tracking
* Secure password setup/reset flows

The backend is designed around real clinic workflows and uses a layered architecture with Controllers, Services, DAOs, Repositories, DTOs, and JPA entities.

---

## Main Roles

The system supports three main roles:

| Role   | Responsibility                                            |
| ------ | --------------------------------------------------------- |
| ADMIN  | Manages staff, schedules, leave, and clinic operations    |
| NURSE  | Registers patients, books appointments, manages queue     |
| DOCTOR | Handles consultations, prescriptions, and patient history |

All APIs are protected using Spring Security and JWT-based role authorization.

---

## Core Features

* JWT authentication and stateless security
* Role-based authorization using Spring Security
* Staff management and activation/deactivation
* Automatic employee ID generation
* Automatic doctor schedule generation
* Weekly doctor schedule management
* Doctor leave exception handling
* Patient registration and search
* Appointment booking with queue generation
* Live queue management
* Doctor consultation workflow
* Prescription and medicine management
* Patient history tracking
* Password setup/reset using secure tokens
* Profile and profile photo support

---

## Tech Stack

| Layer                 | Technology                   |
| --------------------- | ---------------------------- |
| Language              | Java 21                      |
| Framework             | Spring Boot 3.x              |
| Security              | Spring Security + JWT        |
| Database              | PostgreSQL                   |
| ORM                   | Spring Data JPA + Hibernate  |
| Validation            | Jakarta Bean Validation      |
| Email                 | SendGrid                     |
| Storage               | S3-Compatible Object Storage |
| Boilerplate Reduction | Lombok                       |

---

## Backend Architecture

```text
Controller
   ↓
Service Layer
   ↓
DAO Layer
   ↓
Repository Layer
   ↓
PostgreSQL Database
```

### Layer Responsibilities

| Layer      | Responsibility                             |
| ---------- | ------------------------------------------ |
| Controller | Handles REST API requests/responses        |
| Service    | Contains business logic and validations    |
| DAO        | Handles complex database access operations |
| Repository | Spring Data JPA database operations        |
| DTO        | Request and response transfer objects      |
| Entity     | Database models and relationships          |

---

## Project Structure

```text
ClientFlow_Backend/
  src/main/java/com/app/

    config/         Security, JWT, CORS, S3 config
    controller/     REST APIs
    dto/            Request/response DTOs
    entity/         JPA entities
    repository/     Spring Data repositories
    dao/            Data access layer
    service/        Business logic
    exception/      Global exception handling
    enums/          Status and type enums
    mapper/         DTO/entity mappers
    util/           Utility classes
```

---

## Main Entities

| Entity               | Purpose                              |
| -------------------- | ------------------------------------ |
| Role                 | Stores system roles                  |
| Staff                | Stores clinic employees              |
| Patient              | Stores patient details               |
| DoctorSchedule       | Stores recurring doctor availability |
| LeaveException       | Stores doctor leave dates            |
| Appointment          | Stores appointment and queue data    |
| Consultation         | Stores diagnosis and clinical notes  |
| Prescription         | Stores prescription details          |
| PrescriptionMedicine | Stores prescribed medicines          |
| PasswordResetToken   | Stores password reset/setup tokens   |

---

## Important Business Logic

### Appointment Booking Validation

The backend validates:

* Patient exists
* Doctor exists
* Doctor is active
* Doctor has schedule for selected day
* Doctor is not on leave
* Appointment limit not exceeded
* Queue number generated correctly

### Queue Workflow

```text
WAITING
   ↓
IN_PROGRESS
   ↓
COMPLETED
```

Only one patient can be in-progress for a doctor at a time.

### Consultation Locking

After consultation completion:

* Consultation becomes locked
* Prescription becomes locked
* Appointment becomes completed

This prevents accidental modification of medical records.

---

## Security Flow

```text
User Login
   ↓
Validate Credentials
   ↓
Generate JWT Token
   ↓
Frontend Stores Token
   ↓
Bearer Token Sent in APIs
   ↓
JWT Filter Validates Token
   ↓
@PreAuthorize Validates Role
```

---

## Main API Modules

### Authentication

| Method | Endpoint              |
| ------ | --------------------- |
| POST   | /auth/login           |
| POST   | /auth/forgot-password |
| POST   | /auth/reset-password  |

### Admin APIs

| Module           | Purpose             |
| ---------------- | ------------------- |
| Staff Management | Create/manage staff |
| Dashboard        | Clinic statistics   |
| Doctor Schedules | Schedule management |
| Leave Management | Apply doctor leave  |

### Nurse APIs

| Module               | Purpose              |
| -------------------- | -------------------- |
| Patient Registration | Register patients    |
| Appointment Booking  | Book appointments    |
| Live Queue           | Monitor clinic queue |

### Doctor APIs

| Module          | Purpose                |
| --------------- | ---------------------- |
| Queue Dashboard | View waiting patients  |
| Consultation    | Complete consultations |
| Prescription    | Add medicines          |
| Patient History | View previous visits   |

---

## Validation Examples

The backend uses Jakarta Bean Validation and business-level validations.

Examples:

* Duplicate staff email blocked
* Duplicate patient mobile blocked
* Appointment date cannot be in past
* Doctor capacity limit enforced
* Invalid doctor booking blocked
* Duplicate leave entries blocked
* Start time must be before end time

---

## API Response Format

All APIs return a common response structure:

```json
{
  "success": true,
  "message": "Request completed successfully",
  "data": {},
  "errorCode": null
}
```

---

## Environment Variables

```text
DB_HOST
DB_PORT
DB_NAME
DB_USERNAME
DB_PASSWORD
SENDGRID_API_KEY
JWT_SECRET
```

---

## Running the Backend

### Prerequisites

* Java 21
* PostgreSQL
* Maven

### Start Application

Linux/macOS:

```bash
./mvnw spring-boot:run
```

Windows PowerShell:

```powershell
.\mvnw.cmd spring-boot:run
```

Backend runs on:

```text
http://localhost:8080
```

---

## Useful Commands

```bash
./mvnw spring-boot:run
./mvnw test
```

---

## Engineering Challenges Solved

* Managing multiple role-based workflows
* Queue lifecycle management
* Preventing invalid appointment booking
* Designing reusable layered architecture
* Handling doctor schedules and leave separately
* Locking completed consultations and prescriptions
* Maintaining secure JWT-based APIs

---

## Future Improvements

* WebSocket-based live queue updates
* Docker deployment
* CI/CD pipeline integration
* Appointment rescheduling workflow
* Audit logging
* Analytics dashboard
* Pessimistic locking for concurrent queue generation

---

## Related Repository

Frontend Repository:

```text
https://github.com/Full-Stack-Engineering-Bootcamp-2026/ClinicFlow_Frontend
```

---

## Author

Built by Jayraj Gajul , Chetan Asane , Rishita Pathak as a full-stack healthcare workflow management system focused on real-world clinic operations, queue management, consultation workflows, and role-based access control.
