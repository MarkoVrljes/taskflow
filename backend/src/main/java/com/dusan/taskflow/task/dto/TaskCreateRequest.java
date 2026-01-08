package com.dusan.taskflow.task.dto;

import java.time.LocalDate;
import java.util.UUID;

import com.dusan.taskflow.task.TaskPriority;
import com.dusan.taskflow.task.TaskStatus;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record TaskCreateRequest(
        @NotBlank @Size(min = 2, max = 200) String title,
        @Size(max = 5000) String description,
        TaskStatus status,
        TaskPriority priority,
        UUID assigneeId,
        LocalDate dueDate
) {
}
