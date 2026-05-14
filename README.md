# Task Management System

Task Management System based on Spring Boot Microservices with JWT Authentication, Docker, API Gateway and PostgreSQL.

The project contains three main services:

- `auth-service` — user registration, login and JWT token generation
- `task-service` — projects, tasks and comments management
- `api-gateway` — single entry point for all API requests

The project uses two PostgreSQL databases in Docker:

- `postgres-auth` for `auth-service`
- `postgres-task` for `task-service`

---

## Technologies

- Java 21
- Spring Boot
- Spring Security
- JWT Authentication
- Spring Cloud Gateway
- PostgreSQL
- Docker
- Maven
- REST API

---

## Project Structure

```text
web-java-task-management
│
├── auth-service
│   ├── src
│   ├── Dockerfile
│   └── pom.xml
│
├── task-service
│   ├── src
│   ├── Dockerfile
│   └── pom.xml
│
├── api-gateway
│   ├── src
│   ├── Dockerfile
│   └── pom.xml
│
├── pom.xml
└── README.md
```

---

## Requirements

Before running the project, install:

- Java 21
- Maven
- Docker Desktop
- Git

Check installation:

```bash
java -version
mvn -version
docker --version
```

---

## How to run the project with Docker Compose

The easiest way to run the whole project is to use Docker Compose.

Docker Compose starts all required containers:

- `postgres-auth`
- `postgres-task`
- `auth-service-container`
- `task-service-container`
- `api-gateway-container`

### 1. Build the project

Run this command from the root project folder:

```bash
mvn clean package -DskipTests
```

Expected result:

```text
BUILD SUCCESS
```

### 2. Start all services

Run this command from the root project folder:

```bash
docker compose up -d --build
```

This command builds Docker images and starts all containers in the background.

### 3. Check running containers

```bash
docker ps
```

Expected containers:

```text
postgres-auth
postgres-task
auth-service-container
task-service-container
api-gateway-container
```

### 4. Test API Gateway

All requests should go through API Gateway on port `8080`.

Check auth-service through gateway:

```http
GET http://localhost:8080/auth/hello
```

Expected result:

```text
Auth service is running
```

Register user:

```http
POST http://localhost:8080/auth/register
Content-Type: application/json

{
  "username": "composetest",
  "email": "composetest@example.com",
  "password": "123456"
}
```

Expected result:

```json
{
  "message": "User registered successfully"
}
```

Login user:

```http
POST http://localhost:8080/auth/login
Content-Type: application/json

{
  "username": "composetest",
  "password": "123456"
}
```

Expected result:

```json
{
  "token": "JWT_TOKEN_HERE"
}
```

Check secure endpoint:

```http
GET http://localhost:8080/secure/me
Authorization: Bearer JWT_TOKEN_HERE
```

Expected result:

```text
Authenticated as: composetest
```

### 5. Stop all services

```bash
docker compose down
```

### 6. Start again without rebuild

```bash
docker compose up -d
```

### 7. View logs

View logs for all services:

```bash
docker compose logs
```

View logs for one service:

```bash
docker compose logs auth-service
docker compose logs task-service
docker compose logs api-gateway
```

## Docker Compose notes

The file `docker-compose.yml` is located in the root project folder.

It starts:

- two PostgreSQL databases
- auth microservice
- task microservice
- API Gateway

The services use Docker container names for internal communication:

- `auth-service` connects to `postgres-auth`
- `task-service` connects to `postgres-task`
- `api-gateway` forwards requests to `auth-service-container` and `task-service-container`

## How to run the project with Docker

### 1. Clone the repository

```bash
git clone https://github.com/NikolaiKuhar/web-java-task-management.git
cd web-java-task-management
```

---

### 2. Build the project

Run this command from the root project folder:

```bash
mvn clean package -DskipTests
```

Expected result:

```text
BUILD SUCCESS
```

This command builds all modules:

- `auth-service`
- `task-service`
- `api-gateway`

---

### 3. Create Docker network

```bash
docker network create task-management-network
```

This network allows containers to communicate with each other by container name.

---

### 4. Run PostgreSQL for auth-service

```bash
docker run --name postgres-auth ^
-e POSTGRES_DB=task_management_auth ^
-e POSTGRES_USER=postgres ^
-e POSTGRES_PASSWORD=123456 ^
-p 5433:5432 ^
-d postgres:16
```

Connect the database container to the project network:

```bash
docker network connect task-management-network postgres-auth
```

---

### 5. Run PostgreSQL for task-service

```bash
docker run --name postgres-task ^
-e POSTGRES_DB=task_management_tasks ^
-e POSTGRES_USER=postgres ^
-e POSTGRES_PASSWORD=123456 ^
-p 5434:5432 ^
-d postgres:16
```

Connect the database container to the project network:

```bash
docker network connect task-management-network postgres-task
```

---

### 6. Build Docker images

Build `auth-service` image:

```bash
cd auth-service
docker build -t auth-service .
cd ..
```

Build `task-service` image:

```bash
cd task-service
docker build -t task-service .
cd ..
```

Build `api-gateway` image:

```bash
cd api-gateway
docker build -t api-gateway .
cd ..
```

---

### 7. Run application containers

Run `auth-service`:

```bash
docker run --name auth-service-container ^
--network task-management-network ^
-p 8081:8081 ^
-d auth-service
```

Run `task-service`:

```bash
docker run --name task-service-container ^
--network task-management-network ^
-p 8082:8082 ^
-d task-service
```

Run `api-gateway`:

```bash
docker run --name api-gateway-container ^
--network task-management-network ^
-p 8080:8080 ^
-d api-gateway
```

---

### 8. Check running containers

```bash
docker ps
```

Expected containers:

```text
postgres-auth
postgres-task
auth-service-container
task-service-container
api-gateway-container
```

---

## Application Ports

| Service | Port |
|---|---:|
| API Gateway | 8080 |
| Auth Service | 8081 |
| Task Service | 8082 |
| PostgreSQL Auth DB | 5433 |
| PostgreSQL Task DB | 5434 |

Main API entry point:

```text
http://localhost:8080
```

All main requests should be sent through API Gateway.

---

## API Testing

### Check auth-service through gateway

```http
GET http://localhost:8080/auth/hello
```

Expected response:

```text
Auth service is running
```

---

### Register user

```http
POST http://localhost:8080/auth/register
Content-Type: application/json

{
  "username": "dockertest",
  "email": "dockertest@example.com",
  "password": "123456"
}
```

---

### Login user

```http
POST http://localhost:8080/auth/login
Content-Type: application/json

{
  "username": "dockertest",
  "password": "123456"
}
```

Expected response:

```json
{
  "token": "your_jwt_token"
}
```

Use this token for secured requests:

```http
Authorization: Bearer your_jwt_token
```

---

### Check current authenticated user

```http
GET http://localhost:8080/secure/me
Authorization: Bearer your_jwt_token
```

Expected response:

```text
Authenticated as: dockertest
```

---

### Create project

```http
POST http://localhost:8080/projects
Authorization: Bearer your_jwt_token
Content-Type: application/json

{
  "name": "Docker Gateway Project",
  "description": "Project created through api-gateway in Docker"
}
```

---

### Get projects

```http
GET http://localhost:8080/projects
Authorization: Bearer your_jwt_token
```

---

### Create task

```http
POST http://localhost:8080/tasks
Authorization: Bearer your_jwt_token
Content-Type: application/json

{
  "title": "Docker Gateway Task",
  "description": "Task created through api-gateway in Docker",
  "status": "TODO",
  "priority": "HIGH",
  "assigneeId": 1,
  "projectId": 1
}
```

---

### Get tasks

```http
GET http://localhost:8080/tasks
Authorization: Bearer your_jwt_token
```

---

### Update task

```http
PUT http://localhost:8080/tasks/1
Authorization: Bearer your_jwt_token
Content-Type: application/json

{
  "title": "Updated Docker Gateway Task",
  "description": "Updated task through api-gateway in Docker",
  "status": "IN_PROGRESS",
  "priority": "MEDIUM",
  "assigneeId": 1,
  "projectId": 1
}
```

---

### Update task status

```http
PATCH http://localhost:8080/tasks/1/status
Authorization: Bearer your_jwt_token
Content-Type: application/json

{
  "status": "DONE"
}
```

---

### Create comment

```http
POST http://localhost:8080/comments
Authorization: Bearer your_jwt_token
Content-Type: application/json

{
  "content": "Comment created through api-gateway in Docker",
  "taskId": 1
}
```

---

### Get comments by task

```http
GET http://localhost:8080/comments/by-task/1
Authorization: Bearer your_jwt_token
```

---

### Delete task

```http
DELETE http://localhost:8080/tasks/2
Authorization: Bearer your_jwt_token
```

---

## Useful Docker Commands

### View running containers

```bash
docker ps
```

### View all containers

```bash
docker ps -a
```

### View Docker images

```bash
docker images
```

### View logs

```bash
docker logs auth-service-container
docker logs task-service-container
docker logs api-gateway-container
```

### Stop containers

```bash
docker stop api-gateway-container task-service-container auth-service-container postgres-task postgres-auth
```

### Start containers again

```bash
docker start postgres-auth postgres-task auth-service-container task-service-container api-gateway-container
```

### Remove containers

```bash
docker rm -f api-gateway-container task-service-container auth-service-container postgres-task postgres-auth
```

---

## Database Check

You can connect to the auth database:

```bash
docker exec -it postgres-auth psql -U postgres -d task_management_auth
```

Useful commands inside `psql`:

```sql
\dt
SELECT id, username, email FROM users;
\q
```

You can connect to the task database:

```bash
docker exec -it postgres-task psql -U postgres -d task_management_tasks
```

Useful commands inside `psql`:

```sql
\dt
SELECT * FROM projects;
SELECT * FROM tasks;
SELECT * FROM comments;
\q
```

---

## Notes

Inside Docker network services communicate by container names:

```text
auth-service-container
task-service-container
postgres-auth
postgres-task
```

That is why Docker configuration should not use `localhost` for communication between containers.

For example, inside Docker:

```text
http://auth-service-container:8081
```

is correct, but:

```text
http://localhost:8081
```

is incorrect for service-to-service communication inside containers.

---

## Current Status

Implemented and tested:

- user registration
- user login
- JWT token generation
- secured requests with JWT
- project CRUD
- task CRUD
- comment creation and reading
- API Gateway routing
- Docker containers for all services
- separate PostgreSQL containers for auth and task services
- service-to-service communication inside Docker network
