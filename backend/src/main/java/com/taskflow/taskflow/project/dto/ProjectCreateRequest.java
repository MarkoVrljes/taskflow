package com.taskflow.taskflow.project.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ProjectCreateRequest(
        @NotBlank @Size(min = 2, max = 100) String name,
        @Size(max = 2000) String description
) {
}

