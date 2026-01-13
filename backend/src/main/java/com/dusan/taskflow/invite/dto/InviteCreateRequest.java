package com.dusan.taskflow.invite.dto;

import com.dusan.taskflow.workspace.WorkspaceRole;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record InviteCreateRequest(
        @Email @NotBlank String email,
        @NotNull WorkspaceRole role
) {
}
