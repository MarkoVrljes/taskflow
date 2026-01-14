package com.taskflow.taskflow.invite.dto;

import com.taskflow.taskflow.workspace.WorkspaceRole;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record InviteCreateRequest(
        @Email @NotBlank String email,
        @NotNull WorkspaceRole role
) {
}

