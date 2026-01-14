# Dev Log

This file tracks changes made to the project, why they were made, and where they live. It will grow as the project evolves.

## 2025-12-29 - Step 1: Scaffold + local infra

### Summary
- Generated Spring Boot backend (Maven) with core dependencies.
- Added Docker Compose for Postgres.
- Added baseline app config + Flyway init migration.
- Added root README and .gitignore.

### Added
- Backend scaffold under `backend/` with Spring Boot 4.0.1 (Web, JPA, Security, Validation, PostgreSQL, Flyway, Actuator, Lombok).
- Docker Compose for Postgres in `docker-compose.yml`.
- App configuration in `backend/src/main/resources/application.yml`.
- Flyway baseline migration in `backend/src/main/resources/db/migration/V1__init.sql`.
- Root docs: `README.md`, `.gitignore`.

### Notes
- Health endpoint available at `/actuator/health`.
- Flyway runs on startup and validates schema.

## 2025-12-29 - Step 2: Users + auth skeleton (JWT)

### Summary
- Added `users` table and basic auth endpoints (register/login).
- Wired JWT generation and request filter for authenticated routes.
- Added password hashing (BCrypt) and basic security config.

### Added
- Migration: `backend/src/main/resources/db/migration/V2__users.sql`.
- User model + repository:
  - `backend/src/main/java/com/dusan/taskflow/user/User.java`
  - `backend/src/main/java/com/dusan/taskflow/user/UserRepository.java`
- Auth API + DTOs:
  - `backend/src/main/java/com/dusan/taskflow/auth/AuthController.java`
  - `backend/src/main/java/com/dusan/taskflow/auth/AuthService.java`
  - `backend/src/main/java/com/dusan/taskflow/auth/dto/AuthRegisterRequest.java`
  - `backend/src/main/java/com/dusan/taskflow/auth/dto/AuthLoginRequest.java`
  - `backend/src/main/java/com/dusan/taskflow/auth/dto/AuthResponse.java`
- JWT support:
  - `backend/src/main/java/com/dusan/taskflow/auth/jwt/JwtService.java`
  - `backend/src/main/java/com/dusan/taskflow/auth/jwt/JwtAuthenticationFilter.java`
  - `backend/src/main/java/com/dusan/taskflow/auth/jwt/UserPrincipal.java`
- Security config:
  - `backend/src/main/java/com/dusan/taskflow/config/SecurityConfig.java`
- JWT config in `backend/src/main/resources/application.yml`.
- Dependencies: `jjwt-api`, `jjwt-impl`, `jjwt-jackson` added to `backend/pom.xml`.

### Notes
- Open endpoints: `/auth/register`, `/auth/login`, `/actuator/health`.
- All other endpoints require `Authorization: Bearer <token>`.
- JWT secret is a dev placeholder; replace before production.

## 2025-12-29 - Step 3: Workspaces + membership (core)

### Summary
- Added workspace and membership tables plus basic CRUD endpoints.
- Creator is automatically added as OWNER.

### Added
- Migration: `backend/src/main/resources/db/migration/V3__workspaces.sql`.
- Workspace model + repos:
  - `backend/src/main/java/com/dusan/taskflow/workspace/Workspace.java`
  - `backend/src/main/java/com/dusan/taskflow/workspace/WorkspaceMember.java`
  - `backend/src/main/java/com/dusan/taskflow/workspace/WorkspaceMemberId.java`
  - `backend/src/main/java/com/dusan/taskflow/workspace/WorkspaceRole.java`
  - `backend/src/main/java/com/dusan/taskflow/workspace/WorkspaceRepository.java`
  - `backend/src/main/java/com/dusan/taskflow/workspace/WorkspaceMemberRepository.java`
- Workspace API:
  - `backend/src/main/java/com/dusan/taskflow/workspace/WorkspaceController.java`
  - `backend/src/main/java/com/dusan/taskflow/workspace/WorkspaceService.java`
  - `backend/src/main/java/com/dusan/taskflow/workspace/dto/WorkspaceCreateRequest.java`
  - `backend/src/main/java/com/dusan/taskflow/workspace/dto/WorkspaceResponse.java`
- Current user helper:
  - `backend/src/main/java/com/dusan/taskflow/auth/CurrentUserService.java`

### Notes
- Endpoints:
  - `POST /workspaces` creates a workspace and sets creator to OWNER.
  - `GET /workspaces` lists current user's workspaces.
  - `GET /workspaces/{id}` returns a workspace only if the user is a member.

## 2025-12-29 - Step 4: Projects + tasks (core)

### Summary
- Added projects and tasks tables and CRUD endpoints.
- Added task filtering, search, pagination, and sorting.

### Added
- Migration: `backend/src/main/resources/db/migration/V4__projects_tasks.sql`.
- Project model + API:
  - `backend/src/main/java/com/dusan/taskflow/project/Project.java`
  - `backend/src/main/java/com/dusan/taskflow/project/ProjectRepository.java`
  - `backend/src/main/java/com/dusan/taskflow/project/ProjectService.java`
  - `backend/src/main/java/com/dusan/taskflow/project/ProjectController.java`
  - `backend/src/main/java/com/dusan/taskflow/project/dto/ProjectCreateRequest.java`
  - `backend/src/main/java/com/dusan/taskflow/project/dto/ProjectResponse.java`
- Task model + API:
  - `backend/src/main/java/com/dusan/taskflow/task/Task.java`
  - `backend/src/main/java/com/dusan/taskflow/task/TaskRepository.java`
  - `backend/src/main/java/com/dusan/taskflow/task/TaskSpecifications.java`
  - `backend/src/main/java/com/dusan/taskflow/task/TaskService.java`
  - `backend/src/main/java/com/dusan/taskflow/task/TaskController.java`
  - `backend/src/main/java/com/dusan/taskflow/task/TaskStatus.java`
  - `backend/src/main/java/com/dusan/taskflow/task/TaskPriority.java`
  - `backend/src/main/java/com/dusan/taskflow/task/dto/TaskCreateRequest.java`
  - `backend/src/main/java/com/dusan/taskflow/task/dto/TaskUpdateRequest.java`
  - `backend/src/main/java/com/dusan/taskflow/task/dto/TaskResponse.java`

### Notes
- Project endpoints:
  - `POST /workspaces/{workspaceId}/projects`
  - `GET /workspaces/{workspaceId}/projects`
- Task endpoints:
  - `POST /projects/{projectId}/tasks`
  - `GET /workspaces/{workspaceId}/tasks` (supports filters + pagination)
  - `GET /tasks/{taskId}`
  - `PATCH /tasks/{taskId}`
  - `DELETE /tasks/{taskId}`

## 2025-12-29 - Step 5: Comments (core)

### Summary
- Added comments table and endpoints for creating and listing task comments.

### Added
- Migration: `backend/src/main/resources/db/migration/V5__comments.sql`.
- Comment model + API:
  - `backend/src/main/java/com/dusan/taskflow/comment/Comment.java`
  - `backend/src/main/java/com/dusan/taskflow/comment/CommentRepository.java`
  - `backend/src/main/java/com/dusan/taskflow/comment/CommentService.java`
  - `backend/src/main/java/com/dusan/taskflow/comment/CommentController.java`
  - `backend/src/main/java/com/dusan/taskflow/comment/dto/CommentCreateRequest.java`
  - `backend/src/main/java/com/dusan/taskflow/comment/dto/CommentResponse.java`

### Notes
- Endpoints:
  - `POST /tasks/{taskId}/comments`
  - `GET /tasks/{taskId}/comments`

## 2025-12-29 - Step 6: Invites + RBAC (core)

### Summary
- Added workspace invites (create + accept) with token flow.
- Enforced role-based access rules for projects, tasks, and comments.

### Added
- Migration: `backend/src/main/resources/db/migration/V6__workspace_invites.sql`.
- Invite model + API:
  - `backend/src/main/java/com/dusan/taskflow/invite/WorkspaceInvite.java`
  - `backend/src/main/java/com/dusan/taskflow/invite/WorkspaceInviteRepository.java`
  - `backend/src/main/java/com/dusan/taskflow/invite/WorkspaceInviteService.java`
  - `backend/src/main/java/com/dusan/taskflow/invite/WorkspaceInviteController.java`
  - `backend/src/main/java/com/dusan/taskflow/invite/dto/InviteCreateRequest.java`
  - `backend/src/main/java/com/dusan/taskflow/invite/dto/InviteResponse.java`
  - `backend/src/main/java/com/dusan/taskflow/invite/dto/InviteAcceptResponse.java`
- Access helper:
  - `backend/src/main/java/com/dusan/taskflow/workspace/WorkspaceAccessService.java`

### Updated
- Role enforcement in:
  - `backend/src/main/java/com/dusan/taskflow/project/ProjectService.java`
  - `backend/src/main/java/com/dusan/taskflow/task/TaskService.java`
  - `backend/src/main/java/com/dusan/taskflow/comment/CommentService.java`
  - `backend/src/main/java/com/dusan/taskflow/workspace/WorkspaceMemberRepository.java`

### Notes
- Invite endpoints:
  - `POST /workspaces/{workspaceId}/invites` (OWNER/ADMIN)
  - `POST /invites/accept?token=...`

## 2025-12-29 - Step 7: Refresh tokens (core)

### Summary
- Added refresh tokens stored in DB (hashed) with rotation.
- Added refresh and logout endpoints.

### Added
- Migration: `backend/src/main/resources/db/migration/V7__refresh_tokens.sql`.
- Refresh token model + repo:
  - `backend/src/main/java/com/dusan/taskflow/auth/RefreshToken.java`
  - `backend/src/main/java/com/dusan/taskflow/auth/RefreshTokenRepository.java`
- Refresh DTO:
  - `backend/src/main/java/com/dusan/taskflow/auth/dto/AuthRefreshRequest.java`

### Updated
- Auth flow with refresh tokens:
  - `backend/src/main/java/com/dusan/taskflow/auth/AuthService.java`
  - `backend/src/main/java/com/dusan/taskflow/auth/AuthController.java`
  - `backend/src/main/java/com/dusan/taskflow/auth/dto/AuthResponse.java`
  - `backend/src/main/java/com/dusan/taskflow/config/SecurityConfig.java`
  - `backend/src/main/resources/application.yml`

### Notes
- Endpoints:
  - `POST /auth/refresh`
  - `POST /auth/logout`
