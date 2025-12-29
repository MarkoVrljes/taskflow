package com.dusan.taskflow.workspace.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record WorkspaceCreateRequest(
        @NotBlank @Size(min = 2, max = 100) String name
) {
}
