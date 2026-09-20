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

### Environment Variables

Create a `.env` file in the project root based on `.env.example` and provide your own values.

The `.env` file contains sensitive credentials and should not be committed to Git. 

### Start the Resonance Application

```bash
docker compose up --build
```

The `--build` option builds the Resonance Docker image from the project's Dockerfile before starting the application.

When the application starts, Hibernate creates the required database tables inside the Docker MySQL database.

Wait until the Spring Boot application has started successfully before continuing.

### Stop only the Resonance container

Open a **new terminal** in the project directory and run:

```powershell
docker compose stop resonance
```

### Populate the Database

Execute the SQL statements from `database/data.sql` directly against the Docker MySQL database:

```powershell
Get-Content .\database\data.sql | docker exec -i resonance-backend-mysql-1 mysql -u root -p[YOUR_PASSWORD] resonance
```

Replace `[YOUR_PASSWORD]` with the `MYSQL_ROOT_PASSWORD` value from your `.env` file.

This executes `data.sql` directly inside the Docker MySQL database. No local MySQL database or database dump is required.

The order is important:

```text
Docker MySQL starts
        ↓
Spring Boot starts
        ↓
Hibernate creates tables
        ↓
data.sql INSERT statements are executed
        ↓
Database is populated
```

### Restart the Application

After the data has been inserted execute:

```bash
docker compose restart resonance
```

The application will restart and work with the populated database.

The application will be available at:

```text
http://localhost:8080
```

### Starting the Application After Initial Setup

Once the Docker image has been built and the database has been initialized, the application can normally be started with:

```bash
docker compose up
```

> **Warning:** `docker compose down -v` removes the Docker volume and permanently deletes the persisted MySQL data.

---

## API Documentation

Resonance includes Swagger UI for exploring and testing the REST API directly from a web browser.

After starting the application, open:

```text
http://localhost:8080/swagger-ui/index.html
```

Swagger UI allows users to:

- View all available API endpoints
- See request and response formats
- Provide request parameters and request bodies
- Authenticate using JWT
- Execute API requests directly from the browser

---

### Sample User Credentials

The database includes sample users and artists that can be used for testing the API.

**Password for all sample users and artists:**

```text
resonance123
```

---

## Database

The project includes SQL scripts for development.

```text
database/
├── truncate.sql
└── data.sql
```

- `truncate.sql` clears all existing data.
- `data.sql` populates the Docker database with sample users, artists, albums, genres, and songs.

The Docker database is created as:

```text
resonance
```

The application connects to it internally using:

```text
jdbc:mysql://mysql:3306/resonance
```

No local MySQL database is required when running the project with Docker.

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