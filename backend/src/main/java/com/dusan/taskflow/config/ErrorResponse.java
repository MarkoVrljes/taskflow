package com.dusan.taskflow.config;

import java.time.Instant;

public record ErrorResponse(
        String error,
        String message,
        String path,
        Instant timestamp
) {
}
