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

3) Run the frontend:

```bash
cd frontend
npm install
npm run dev
```

Frontend: http://localhost:5173
Swagger: http://localhost:8080/swagger-ui/index.html

Health check:

- http://localhost:8080/actuator/health
