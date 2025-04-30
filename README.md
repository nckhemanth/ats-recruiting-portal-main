
# ATS Recruiting Portal

A full-stack applicant tracking system for public careers discovery, candidate applications, recruiter job management, and structured hiring pipelines. The interface recreates the Recruit Flow experience with an independent Java and React implementation.

## Stack

See the [architecture package](architecture/README.md) for the system picture, component boundaries, data ownership, and candidate-application sequence.

- Java 21 and Spring Boot 3
- Spring Security with signed JWT sessions and role-based access control
- Spring Data JPA/Hibernate, PostgreSQL, Flyway, and pageable specification queries
- Elasticsearch for indexed role search, enabled in the Docker environment
- React 19, TypeScript, React Router, Axios, and Vite
- Docker multi-stage builds and Docker Compose

## Workflows

- Public careers search and role details
- Candidate registration, login, reusable profile, resume upload, and application tracking
- Recruiter registration, job creation, configurable pipeline stages, applicant movement, resumes, and internal notes
- DTO-only API responses, recruiter ownership checks, upload validation, pagination, batch-fetch tuning, and database indexes

## Run locally

```bash
docker compose up --build
```

Open `http://localhost:3000`. The API health endpoint is `http://localhost:8080/api/health`.

The local Docker environment seeds two demonstration accounts:

- Recruiter: `recruiter@recruitflow.dev` / `Recruiter123!`
- Candidate: `candidate@recruitflow.dev` / `Candidate123!`

Demo seeding is disabled unless `SEED_DEMO=true`.

## Verify

```bash
cd backend && ./mvnw test
cd ../frontend && npm ci && npm run lint && npm run build
```

## Configuration

Copy `.env.example` to `.env` before changing local database credentials or secrets. Use a strong unique JWT secret and managed PostgreSQL/Elasticsearch services in production.
