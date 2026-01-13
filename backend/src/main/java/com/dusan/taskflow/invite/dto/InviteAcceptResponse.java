package com.dusan.taskflow.invite.dto;

import java.util.UUID;

import com.dusan.taskflow.workspace.WorkspaceRole;

public record InviteAcceptResponse(
        UUID workspaceId,
        WorkspaceRole role
) {
}
