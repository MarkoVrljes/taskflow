# Taskflow

Local-first monorepo for a team task manager.

## Quickstart

1) Start Postgres:

```bash
docker compose up -d
```

2) Run the backend:

```bash
cd backend
./mvnw spring-boot:run
```

Health check:

- http://localhost:8080/actuator/health