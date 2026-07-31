# Lotusplus Gamification

Backend service for gamification features including user profile, point management, reward system, and daily check-in.

# Scope & Authentication Note

This project focuses on implementing core gamification business features:

- User profile management
- Point transaction and history
- Reward configuration
- Daily check-in mechanism
- Redis caching
- Distributed locking
- Database migration with Liquibase

Authentication and authorization are simplified for demonstration purposes.
## Tech Stack

### Backend
- Java 21
- Spring Boot 4
- Spring Data JPA
- Spring Security
- Spring Cache
- Liquibase

### Database & Infrastructure
- PostgreSQL 17
- Redis 8
- Redisson Distributed Lock
- Docker / Docker Compose

### Development Tools
- Maven
- Postman
- Git

---

# Features

## User
- Create user
- Get user profile
- Cache user profile using Redis

## Point
- Get point history
- Deduct user points
- Maintain point transaction history

## Check-in
- Daily check-in
- Prevent duplicate check-in using distributed lock
- Configurable check-in time window

## Reward
- Reward configuration management
- Cached reward configuration

---

# Project Structure

```
lotusplus-gamification
│
├── src/main/java/com/example/lotusplus
│
│   ├── common
│   │   ├── config
│   │   ├── exception
│   │   ├── cache
│   │   └── lock
│   │
│   ├── user
│   │
│   ├── point
│   │
│   ├── reward
│   │
│   └── checkin
│
├── docs
│   └── postman
│       ├── Lotusplus.postman_collection.json
│       └── Lotusplus.postman_environment.json
│
├── Dockerfile
├── docker-compose.yml
└── pom.xml
```

---

# Requirements

Before running the project, install:

- Java 21+
- Docker
- Docker Compose

---

# Run With Docker Compose

The project includes:

- Spring Boot application
- PostgreSQL
- Redis
- Redis Insight
- PgAdmin


Build and start:

```bash
docker compose up --build
```

Application will start at:

```
http://localhost:8080
```

---

# Services

| Service | Port |
|---|---|
| Spring Boot API | 8080 |
| PostgreSQL | 5432 |
| Redis | 6379 |
| Redis Insight | 5540 |
| PgAdmin | 5050 |

---

# Database Configuration

PostgreSQL:

```
Database: lotusplus
Username: postgres
Password: postgres
Port: 5432
```

Liquibase automatically runs database migrations on startup.

---

# Redis Configuration

Redis:

```
Host: localhost
Port: 6379
```

Redis is used for:

- Application cache
- Distributed lock
- Temporary data storage

---

# Run Without Docker

Start PostgreSQL and Redis manually, then:

```bash
./mvnw spring-boot:run
```

or:

```bash
mvn spring-boot:run
```

---

# API Documentation

Import Postman files:

```
docs/postman/
│
├── Lotusplus.postman_collection.json
```

Postman environment:

```
base_url=http://localhost:8080
```

---

# API Overview

## Users

### Create User

```
POST /api/v1/users
```

Request:

```json
{
  "username": "fantus",
  "avatar": "https://test.com/avatar.png"
}
```

---

### Get User Profile

```
GET /api/v1/users/{userId}
```

---

## Points

### Get Point History

```
GET /api/v1/points/history/{userId}?page=0&size=10
```

---

### Deduct Point

```
POST /api/v1/points/deduct
```

Request:

```json
{
  "userId": "uuid",
  "point": 10,
  "description": "Redeem reward"
}
```

---

## Check-in

### Check-in

```
POST /api/v1/checkins?userId={userId}
```

---

### Get Check-in Status

```
GET /api/v1/checkins/status?userId={userId}
```

---

# Environment Variables

Docker environment:

```properties
SPRING_PROFILES_ACTIVE=docker

SPRING_DATASOURCE_URL=jdbc:postgresql://postgres:5432/lotusplus
SPRING_DATASOURCE_USERNAME=postgres
SPRING_DATASOURCE_PASSWORD=postgres

SPRING_DATA_REDIS_HOST=redis
SPRING_DATA_REDIS_PORT=6379
```

---

# Git Workflow

Feature branches:

```
feature/*
fix/*
refactor/*
docs/*
```

Example:

```bash
git checkout -b feature/new-feature

git add .

git commit -m "feat: implement new feature"

git push origin feature/new-feature
```

---

# License

This project is for learning and demonstration purposes.
