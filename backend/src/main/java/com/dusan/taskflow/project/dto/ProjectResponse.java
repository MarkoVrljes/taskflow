package com.dusan.taskflow.project.dto;

import java.util.UUID;

public record ProjectResponse(UUID id, UUID workspaceId, String name, String description) {
}
