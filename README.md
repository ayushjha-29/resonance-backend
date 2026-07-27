# Resonance

Resonance is a Spotify-inspired music streaming backend built with Spring Boot. The project focuses on building a scalable REST API while following clean architecture and backend best practices.

## Tech Stack

- Java
- Spring Boot
- Spring Security
- JWT (JSON Web Tokens)
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
- ✅ Spring Security
- ✅ JWT Authentication
- ✅ Role-based Authorization
- ✅ Password Encryption (BCrypt)

## Planned Features

- File Upload & Download
- Email Integration
- Pagination & Sorting
- Dynamic Searching (Specifications)
- Caching
- Unit & Integration Testing
- Docker
- Deployment

## Security Features

- JWT-based Authentication
- Stateless Session Management
- BCrypt Password Encoding
- Custom UserDetailsService
- JWT Authentication Filter
- Public Authentication Endpoints
- Protected API Endpoints
- Role-based Access Control
  - USER
  - ARTIST
  - ADMIN

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
- MapStruct handles entity-DTO conversion.
- Spring Security protects endpoints using JWT authentication.

## Project Status

🚧 Under Development

Core backend functionality and authentication have been completed. Upcoming work includes file handling, email integration, pagination, searching, caching, testing, Docker, and deployment.