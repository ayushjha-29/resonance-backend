# Resonance

Resonance is a music catalog REST API built with Spring Boot. The project focuses on building a secure, scalable backend for managing music metadata such as artists, albums, songs, and genres, while following clean architecture and modern backend development practices.

---

## Tech Stack

- Java 21
- Spring Boot
- Spring Security
- JWT (JSON Web Tokens)
- Spring Data JPA
- Hibernate
- MySQL
- MapStruct
- Thymeleaf
- Java Mail Sender
- Maven
- Lombok

---

## Features

### Authentication & Authorization

- ✅ User Registration
- ✅ Artist Registration
- ✅ Login using Username or Email
- ✅ JWT Authentication
- ✅ Role-Based Authorization
- ✅ BCrypt Password Encoding
- ✅ Stateless Authentication

### Email Integration

- ✅ Email Verification
- ✅ Welcome Email
- ✅ Forgot Password
- ✅ Password Reset
- ✅ Asynchronous Email Sending
- ✅ HTML Email Templates using Thymeleaf

### Backend Features

- ✅ CRUD APIs
- ✅ Request & Response DTOs
- ✅ Validation
- ✅ Global Exception Handling
- ✅ MapStruct DTO Mapping
- ✅ Pagination & Sorting
- ✅ Dynamic Searching (Specifications)
- ✅ Caching

### Database Design

- ✅ One-to-One
- ✅ One-to-Many
- ✅ Many-to-One
- ✅ Many-to-Many

---

## Security

- JWT-based Authentication
- Stateless Session Management
- Custom UserDetailsService
- JWT Authentication Filter
- BCrypt Password Encoding
- Email Verification before Login
- Login using Username or Email
- Role-Based Access Control

### Roles

- USER
- ARTIST
- ADMIN

---

## Project Structure

```text
Controller
    │
    ▼
Service
    │
    ▼
Repository
    │
    ▼
MySQL Database
```

The project follows a layered architecture where:

- Controllers handle incoming HTTP requests.
- Services contain business logic.
- Repositories interact with the database.
- MapStruct performs Entity ↔ DTO conversion.
- Spring Security secures protected endpoints.

---

## Upcoming Features

- Unit Testing
- Integration Testing
- Docker
- Deployment

---

## Database

The project includes SQL scripts for development.

```text
database/
├── truncate.sql
└── seed.sql
```

- `truncate.sql` clears all existing data.
- `seed.sql` populates the database with sample users, artists, albums, genres, and songs.

---

## Current Status

🚧 **Actively Under Development**

Completed:

- Authentication & Authorization
- Email Integration
- Security
- CRUD Operations
- DTO Mapping
- Validation
- Exception Handling
- Database Relationships
- Pagination & Sorting
- Dynamic Searching (Specifications)
- Caching

The remaining work focuses on improving scalability, testing, deployment, and production readiness.