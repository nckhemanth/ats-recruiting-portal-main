# High-Level Design

## Goals

The portal serves anonymous job seekers, authenticated candidates, and recruiters from one API while enforcing role and job-ownership boundaries. PostgreSQL remains authoritative; Elasticsearch is a derived search index used for fast text discovery.

## System boundaries

![System architecture](diagrams/system-architecture.svg)

| Component | Technology | Responsibility |
|---|---|---|
| Browser application | React 19, TypeScript, Vite, React Router | Public careers, candidate profile/applications, recruiter jobs and pipeline UI |
| Edge container | Nginx | Serves the static application and provides the browser entry point |
| API | Java 21, Spring Boot 3 | REST contracts, validation, transactions, ownership checks, pagination, and orchestration |
| Identity boundary | Spring Security, signed JWT | Registration/login, token verification, candidate/recruiter RBAC |
| Transactional store | PostgreSQL, JPA/Hibernate, Flyway | Users, jobs, stages, applications, notes, and indexes |
| Search projection | Elasticsearch | Multi-field role search weighted toward title and company |
| Resume store | Mounted Docker volume locally | Uploaded candidate resumes; replace with managed object storage in production |

## Domain model

- A recruiter owns job postings and their ordered pipeline stages.
- A candidate owns a reusable account/profile and may apply once per job.
- A job application references the candidate, job, current stage, resume key, cover letter, and notes.
- Recruiter mutations call ownership checks before reading resumes, moving stages, or writing notes.

## Request paths

Public job queries use pageable JPA specifications for structured filters. When Elasticsearch is enabled, text searches return matching job IDs and PostgreSQL supplies authoritative job views. Candidate and recruiter APIs require JWT authentication and role-specific authorization.

## Consistency model

PostgreSQL is the system of record. The Elasticsearch document is a projection of an open job and may be rebuilt. Search indexing should be triggered after the database transaction commits; production deployments should use an outbox or retry queue so search failures never roll back authoritative hiring data.

## Security

- BCrypt-backed credentials and signed JWT sessions with a twelve-hour configured lifetime.
- Candidate/recruiter role checks at the HTTP boundary plus recruiter ownership checks in services.
- DTO-only responses prevent accidental entity or credential exposure.
- Resume content type, size, and storage name must be validated server-side.
- Production should use short-lived tokens, secret rotation, private data services, malware scanning, audit events, and managed object storage.

## Performance and scaling

- Pageable specifications and database indexes bound public query cost.
- Hibernate batch and default batch-fetch sizes reduce N+1 behavior.
- The stateless API can scale horizontally; PostgreSQL and Elasticsearch require connection/concurrency limits.
- Resume downloads should use signed object URLs rather than proxying large files through API replicas.
- Cache only public job summaries and invalidate on job updates.

## Deployment

`compose.yaml` runs Nginx/React, Spring Boot, PostgreSQL, Elasticsearch, and a persistent upload volume. A production layout separates these into independently monitored services behind TLS and replaces local volumes with durable object storage.
