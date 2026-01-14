package com.taskflow.taskflow.invite.dto;

import java.time.Instant;
import java.util.UUID;

import com.taskflow.taskflow.workspace.WorkspaceRole;

public record InviteResponse(
        UUID id,
        UUID workspaceId,
        String email,
        WorkspaceRole role,
        String token,
        Instant expiresAt,
        Instant acceptedAt
) {
}

