package com.taskflow.taskflow.invite.dto;

import java.util.UUID;

import com.taskflow.taskflow.workspace.WorkspaceRole;

public record InviteAcceptResponse(
        UUID workspaceId,
        WorkspaceRole role
) {
}

