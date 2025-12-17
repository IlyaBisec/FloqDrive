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

---

## Project architecture

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

## PostgreSQL + Docker

PostgreSQL runs in a Docker container.

### PostgreSQL:

```bash
docker run --name drive-postgres \
  -e POSTGRES_DB=drive \
  -e POSTGRES_USER=postgres \
  -e POSTGRES_PASSWORD=postgres \
  -p 5432:5432 \
  -d postgres:15
```

### Connection to `application.yml`:

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

## 📖 Swagger

Swagger is used as UI documentation for APIs..

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

## How to Run

### Requirements
- Java 17+
- Docker & Docker Compose

### Steps
1. Clone the repository
2. Start PostgreSQL using Docker:
3. Start backend (`DriveApplication`)
3. Open Swagger
4. (Optionally) start frontend

---

## 📎 IlyaBisec

The project was developed as a pet project to prepare for a backend position.

