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
- Docker
- Docker Compose

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

## Testing

- ✅ Unit Testing
- ✅ Spring Boot Application Context Test
  - Application Context Loading
  - Spring Bean Initialization & Dependency Injection
  - JPA & Database Configuration Validation
  - Security Configuration Loading

---

## Docker

The application is containerized using Docker and Docker Compose.

The Docker setup consists of two services:

- **Resonance** — Spring Boot application
- **MySQL** — MySQL 8.4 database

The services communicate through the Docker Compose network, with Resonance connecting to MySQL using the service name:

```text
jdbc:mysql://mysql:3306/resonance
```

### Running with Docker

Make sure Docker Desktop is installed and running.

Clone the repository and navigate to the project directory:

```bash
git clone https://github.com/ayushjha-29/resonance-backend.git
cd resonance-backend
```

Start the application:

```bash
docker compose up --build
```

The `--build` option builds the Resonance Docker image before starting the containers.

After the image has been built once, the application can normally be started with:

```bash
docker compose up
```

The application will be available at:

```text
http://localhost:8080
```

To run the containers in the background:

```bash
docker compose up -d
```

To stop the containers:

```bash
docker compose stop
```

To start previously stopped containers:

```bash
docker compose start
```

To stop and remove the containers:

```bash
docker compose down
```

The MySQL data is stored in a Docker named volume and is preserved when using `docker compose down`.

> **Warning:** `docker compose down -v` removes the Docker volume and permanently deletes the persisted MySQL data.

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

## Project Status

✅ **Complete**

Resonance has completed its planned backend development and currently includes:

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
- OpenAPI / Swagger UI
- Unit Testing
- Spring Boot Application Context Test
- Docker & Docker Compose

The project was built as a production-style Spring Boot backend to practice modern backend development, security, database design, testing, and containerization.