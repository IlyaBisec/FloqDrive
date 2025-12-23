# FloqDrive

FloqDrive is a **RESTful backend application** built with **Spring Boot** that provides:
- User registration and authentication using **JWT**
- Secure file upload, download, deletion, and listing
- File storage on disk with metadata stored in a database
- Stateless security using **Spring Security**

## Functional (MVP)

- User registration
- Login and JWT receipt
- File uploading
- Viewing the user's file list
- Downloading files
- Deleting files
- Access control (the user can only see their own files)
  
- See [FEATURES.md](FEATURES.md) for planned improvements and roadmap.

---

## Project architecture

```
FloqDrive/
├ gradlew
├ gradle/
├ settings.gradle
├ build.gradle
├ docker-compose.yml
├ wait-for-postgres.sh
├ backend/
│ ├ build.gradle
│ ├ Dockerfile
│ └ src/
│ 	├- java/
│ 	└- resources/
└ frontend/
```

Project built in **layered architecture**:

- **Controller layer** – handles HTTP requests and responses
- **Service layer** – contains business logic
- **Repository layer** – database access via JPA
- **Security layer** – JWT filter and Spring Security configuration

Authentication is implemented using **JWT tokens**. After login, the client must send the token in the `Authorization` header:

```
Authorization: Bearer <JWT_TOKEN>
```

### Backend structure

```
src/main/java/com/floqdrive
├── controller     // REST API
├── service        // business logic
├── repository     // JPA repositories
├── entity         // JPA entities
├── dto            // DTO for requests and responses
├── security       // JWT + Spring Security
├── exception      // error handling
└── FloqDriveApplication.java
```

- Controllers don't know about the database
- Services don't depend on HTTP
- Repositories don't contain business logic

---

## Security and JWT

Authentication is implemented through **JWT (JSON Web Token)**.

### Authentication algorithm:

1. User logs in (`/auth/login`)
2. Backend returns JWT
3. The client saves JWT
4. JWT is passed in `Authorization: Bearer <token>`
5. `JwtFilter`:
   - checks the token
   - retrieves userId
   - puts the user in `SecurityContext`
6. Controllers get the current user

> The application is stateless—the server does not store sessions.

---

## Stack

### Backend
- **Java 17**
- **Spring Boot 3**
- **Spring Security**
- **JWT (jjwt)**
- **Spring Data JPA (Hibernate)**
- **PostgreSQL**
- **Docker**
- **Swagger**
- **Lombok**
- **Gradle**

### Frontend
- **React**
- **Axios**
- **JWT в localStorage**

---

## How to Run

### Production-like

FloqDrive is designed to run in a production-like Docker environment using Docker Compose.

#### Architecture

- Backend: Spring Boot (Java 17)
- Database: PostgreSQL 15
- Build: Gradle multi-module
- Startup handling: wait-for-db script
- Configuration: environment variables

#### Docker Compose

The application is started using Docker Compose:

```
docker compose build --no-cache
docker compose up -d
```
This will start:
- **floqdrive-backend** - Spring Boot application
- **floqdrive-postgres** - PostgreSQL database

#### Environment Variables

Backend configuration is injected via environment variables:

```
SPRING_DATASOURCE_URL=jdbc:postgresql://floqdrive-postgres:5432/floqdrive
SPRING_DATASOURCE_USERNAME=floq
SPRING_DATASOURCE_PASSWORD=floq
SPRING_JPA_HIBERNATE_DDL_AUTO=update
```
This allows the same application to run locally or inside Docker without code changes.

##### Database Readiness

Docker Compose **depends_on** does not guarantee database readiness.

To handle this, the backend container uses a **wait-for-postgres** script that:
- waits until PostgreSQL is reachable
- starts the application only after the database is available

This prevents startup failures in production environments.

##### Logs

```
docker compose logs -f backend
```
##### Stop services

```
docker compose down
```
Database data is persisted using Docker volumes.

---

### Localhost

PostgreSQL runs in a Docker container.

#### PostgreSQL:

```bash
docker run --name drive-postgres \
  -e POSTGRES_DB=drive \
  -e POSTGRES_USER=postgres \
  -e POSTGRES_PASSWORD=postgres \
  -p 5432:5432 \
  -d postgres:15
```

#### Connection to `application.yml`:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/drive
    username: postgres
    password: postgres

  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true
    properties:
      hibernate:
        format_sql: true

server:
  port: 8080
```

---

## File Storage Logic

- Files are stored on disk in the following structure:

```
uploads/
 └── {userId}/
      └── {uuid_filename}
```

- File metadata is stored in the database
- Each file belongs to a specific user
- Access is restricted: users can only manage their own files

For each file:
- original name
- unique saved name
- size
- upload date
- owner

---

## Swagger

Swagger is used as UI documentation for APIs.

After starting the application:
```
http://localhost:8080/swagger-ui.html
```

---

## API Endpoints

### Auth
- `POST /api/auth/register` – register new user
- `POST /api/auth/login` – login and receive JWT

### Files (JWT required)
- `POST /api/files/upload` – upload file
- `GET /api/files` – list user files
- `GET /api/files/{fileId}` – download file
- `DELETE /api/files/{fileId}` – delete file

---

## Error Handling

The application uses a **global exception handler**:
- `404 Not Found` – entity not found or access denied
- `400 Bad Request` – validation or logical errors

---

## IlyaBisec

The project focuses on clean architecture, stateless security,
and production-ready containerized deployment, because was developed as a pet project to prepare for a backend position.

