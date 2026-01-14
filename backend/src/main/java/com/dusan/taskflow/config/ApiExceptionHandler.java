package com.dusan.taskflow.config;

import java.time.Instant;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class ApiExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(ApiExceptionHandler.class);

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ErrorResponse> handleResponseStatus(
            ResponseStatusException ex,
            HttpServletRequest request) {
        logger.warn("Request failed: {}", request.getRequestURI(), ex);
        ErrorResponse body = new ErrorResponse(
                ex.getStatusCode().toString(),
                ex.getReason(),
                request.getRequestURI(),
                Instant.now());
        return ResponseEntity.status(ex.getStatusCode()).body(body);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneric(
            Exception ex,
            HttpServletRequest request) {
        logger.error("Unhandled error: {}", request.getRequestURI(), ex);
        ErrorResponse body = new ErrorResponse(
                "INTERNAL_SERVER_ERROR",
                ex.getMessage(),
                request.getRequestURI(),
                Instant.now());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationErrorResponse> handleValidation(
            MethodArgumentNotValidException ex,
            HttpServletRequest request) {
        List<ValidationErrorResponse.FieldViolation> details = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(this::toFieldViolation)
                .toList();
        ValidationErrorResponse body = new ValidationErrorResponse(
                "VALIDATION_ERROR",
                "Request validation failed",
                request.getRequestURI(),
                Instant.now(),
                details);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    private ValidationErrorResponse.FieldViolation toFieldViolation(FieldError error) {
        String message = error.getDefaultMessage() == null ? "Invalid value" : error.getDefaultMessage();
        return new ValidationErrorResponse.FieldViolation(error.getField(), message);
    }
}
