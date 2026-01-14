package com.dusan.taskflow.auth.dto;

public record AuthResponse(String accessToken, String refreshToken, String tokenType) {
}
