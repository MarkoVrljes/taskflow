package com.dusan.taskflow.task.dto;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

import com.dusan.taskflow.task.TaskPriority;
import com.dusan.taskflow.task.TaskStatus;

public record TaskResponse(
        UUID id,
        UUID workspaceId,
        UUID projectId,
        String title,
        String description,
        TaskStatus status,
        TaskPriority priority,
        UUID assigneeId,
        UUID createdBy,
        LocalDate dueDate,
        long version,
        Instant createdAt,
        Instant updatedAt
) {
}
