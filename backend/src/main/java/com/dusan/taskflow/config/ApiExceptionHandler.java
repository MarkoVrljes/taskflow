package com.dusan.taskflow.config;

import java.util.LinkedHashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class ApiExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(ApiExceptionHandler.class);

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<Map<String, Object>> handleResponseStatus(
            ResponseStatusException ex,
            HttpServletRequest request) {
        logger.warn("Request failed: {}", request.getRequestURI(), ex);
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("error", ex.getStatusCode().toString());
        body.put("message", ex.getReason());
        body.put("path", request.getRequestURI());
        return ResponseEntity.status(ex.getStatusCode()).body(body);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGeneric(
            Exception ex,
            HttpServletRequest request) {
        logger.error("Unhandled error: {}", request.getRequestURI(), ex);
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("error", "INTERNAL_SERVER_ERROR");
        body.put("message", ex.getMessage());
        body.put("path", request.getRequestURI());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
    }
}
