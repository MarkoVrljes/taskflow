package com.dusan.taskflow.config;

import java.time.Instant;
import java.util.List;

public record ValidationErrorResponse(
        String error,
        String message,
        String path,
        Instant timestamp,
        List<FieldViolation> details
) {
    public record FieldViolation(String field, String message) {
    }
}
