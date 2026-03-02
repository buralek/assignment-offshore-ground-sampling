# Offshore Ground Sampling

A full-stack web application for managing offshore soil sampling measurements. Supports CRUD operations, location-based filtering, cursor-based pagination, threshold violation highlighting, and a real-time statistics dashboard.

---

## Tech Stack

### Backend

| Technology | Version                | Purpose                                |
|---|------------------------|----------------------------------------|
| Java | 25                     | Language                               |
| Spring Boot | 4.0.3                  | Application framework                  |
| Spring Data JPA / Hibernate | handled by Spring Boot | ORM and persistence                    |
| H2 | handled by Spring Boot | In-memory database (resets on restart) |
| Spring Security + OAuth2 Resource Server | handled by Spring Boot | JWT-based stateless authentication     |
| Springdoc OpenAPI | 3.0.1                  | Swagger UI at `/swagger-ui.html`       |
| Lombok | handled by Spring Boot | Boilerplate reduction                  |
| Maven | 3.9.12 | Build tool                             |

**Architecture:** Hexagonal (ports and adapters) with three layers — `domain`, `application`, `infrastructure`.

### Frontend

| Technology | Version | Purpose |
|---|---|---|
| Angular | 21 | SPA framework |
| Angular Material | 21 | UI component library |
| TypeScript | 5.9 | Language |
| RxJS | 7.8 | Reactive HTTP and async |
| Vitest | 4 | Unit testing |
| Prettier | 3 | Code formatting |

### Infrastructure

| Technology | Purpose |
|---|---|
| Docker | Container runtime |
| Docker Compose | Multi-service orchestration |
| Nginx | Serves the Angular SPA and reverse-proxies `/api` to the backend |

---

## Project Structure

```
.
├── backend/          # Spring Boot application
│   ├── src/
│   │   └── main/
│   │       ├── java/buralek/assignment/ground/
│   │       │   ├── domain/          # Models, ports, domain services
│   │       │   ├── application/     # Controllers, DTOs, config
│   │       │   └── infrastructure/  # JPA entities, repositories, adapters
│   │       └── resources/
│   │           ├── application.yaml
│   │           └── data.sql         # Seed data: 4 locations, 20 samples
│   └── Dockerfile
├── frontend/         # Angular application
│   ├── src/app/
│   │   ├── auth/            # Login page, JWT interceptor, auth guard
│   │   ├── home/            # Root layout (toolbar + logout)
│   │   ├── samples/         # Sample table, form dialog, delete dialog
│   │   ├── statistics/      # Statistics cards
│   │   └── shared/          # Models, services (Filter, Location, Sample, Statistics)
│   ├── nginx.conf
│   └── Dockerfile
└── docker-compose.yml
```

---

## Running with Docker Compose

### Prerequisites

- [Docker](https://docs.docker.com/get-docker/) with Docker Compose v2

### Start

```bash
docker compose up --build
```

This builds and starts both services:

| Service | URL |
|---|---|
| Frontend | http://localhost:4200 |
| Backend API | http://localhost:8080 |
| Swagger UI | http://localhost:8080/swagger-ui.html |

### Stop

```bash
docker compose down
```

---

## Default Credentials

| Field | Value |
|---|---|
| Username | `admin` |
| Password | `admin123` |

> Credentials are configured in `backend/src/main/resources/application.yaml` under `application.security.admin`.

---

## Running Locally (without Docker)

### Backend

Requires JDK 25.

```bash
cd backend
./mvnw spring-boot:run
```

API available at `http://localhost:8080`. Swagger UI at `http://localhost:8080/swagger-ui.html`.

### Frontend

Requires Node.js 22+ and npm 11+.

```bash
cd frontend
npm install
npm start
```

App available at `http://localhost:4200`. The Angular dev server proxies `/api` to `http://localhost:8080` via `proxy.conf.json`.

### Tests

```bash
# Backend
cd backend && ./mvnw test

# Frontend
cd frontend && npm test
```

---

## Key Features

- **JWT authentication** — stateless login; token stored in `sessionStorage`
- **Sample management** — create, edit, and delete ground samples with location, timestamp, unit weight, water content, and shear strength
- **Location filter** — filter the table and statistics by platform location
- **Cursor-based pagination** — loads 10 samples at a time; "Load more" appends the next page
- **Threshold highlighting** — rows and cells exceeding configured limits are visually flagged with warning icons
- **Unit system toggle** — switch between metric (kN/m³, %, kPa) and US customary units
- **Statistics dashboard** — server-computed average water content and per-metric exceeded-threshold counts; updates automatically on filter change and after any data mutation