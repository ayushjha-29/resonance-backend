# Resonance

Resonance is a Spotify-inspired music streaming backend built with Spring Boot. The project focuses on building a scalable REST API while following clean architecture and backend best practices.

## Tech Stack

- Java
- Spring Boot
- Spring Data JPA
- Hibernate
- MySQL
- Maven
- MapStruct

## Features Completed

- ✅ CRUD APIs
- ✅ Request & Response DTOs
- ✅ Validation
- ✅ Global Exception Handling
- ✅ JPA Relationships
    - One-to-One
    - One-to-Many
    - Many-to-One
    - Many-to-Many
- ✅ MapStruct for DTO mapping

## Planned Features

- Spring Security + JWT Authentication
- File Upload & Download
- Email Integration
- Pagination & Sorting
- Dynamic Searching (Specifications)
- Caching
- Unit & Integration Testing
- Docker
- Deployment

## Project Structure

```text
Controller
   ↓
Service
   ↓
Repository
   ↓
Database
```

The project follows a layered architecture where:

- Controllers handle HTTP requests and responses.
- Services contain business logic.
- Repositories interact with the database.
- MapStruct is used for entity-DTO conversion.

## Project Status

🚧 Under Development

The core CRUD functionality has been completed. Upcoming work includes security, file handling, advanced querying, testing, and deployment.