package com.taskflow.taskflow.comment.dto;

import java.time.Instant;
import java.util.UUID;

public record CommentResponse(
        UUID id,
        UUID taskId,
        UUID workspaceId,
        UUID authorId,
        String body,
        Instant createdAt,
        Instant updatedAt
) {
}

