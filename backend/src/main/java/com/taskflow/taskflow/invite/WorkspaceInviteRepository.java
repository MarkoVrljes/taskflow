package com.taskflow.taskflow.invite;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface WorkspaceInviteRepository extends JpaRepository<WorkspaceInvite, UUID> {
    Optional<WorkspaceInvite> findByToken(String token);

    Optional<WorkspaceInvite> findByTokenAndAcceptedAtIsNullAndExpiresAtAfter(String token, Instant now);
}

