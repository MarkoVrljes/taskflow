package com.dusan.taskflow.auth;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.Locale;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.dusan.taskflow.auth.dto.AuthLoginRequest;
import com.dusan.taskflow.auth.dto.AuthRefreshRequest;
import com.dusan.taskflow.auth.dto.AuthRegisterRequest;
import com.dusan.taskflow.auth.dto.AuthResponse;
import com.dusan.taskflow.auth.jwt.JwtService;
import com.dusan.taskflow.user.User;
import com.dusan.taskflow.user.UserRepository;

@Service
public class AuthService {

    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final RefreshTokenRepository refreshTokenRepository;
    private final long refreshExpirationDays;
    private final CurrentUserService currentUserService;

    public AuthService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService,
            RefreshTokenRepository refreshTokenRepository,
            @org.springframework.beans.factory.annotation.Value("${app.refresh.expiration-days}") long refreshExpirationDays,
            CurrentUserService currentUserService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.refreshTokenRepository = refreshTokenRepository;
        this.refreshExpirationDays = refreshExpirationDays;
        this.currentUserService = currentUserService;
    }

    public AuthResponse register(AuthRegisterRequest request) {
        String email = normalizeEmail(request.email());
        if (userRepository.findByEmailIgnoreCase(email).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already registered");
        }

        User user = new User();
        user.setEmail(email);
        user.setPasswordHash(passwordEncoder.encode(request.password()));
        userRepository.save(user);

        String accessToken = jwtService.generateToken(user.getId());
        String refreshToken = issueRefreshToken(user.getId());
        return new AuthResponse(accessToken, refreshToken, "Bearer");
    }

    public AuthResponse login(AuthLoginRequest request) {
        String email = normalizeEmail(request.email());
        User user = userRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials"));

        if (!passwordEncoder.matches(request.password(), user.getPasswordHash())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials");
        }

        String accessToken = jwtService.generateToken(user.getId());
        String refreshToken = issueRefreshToken(user.getId());
        return new AuthResponse(accessToken, refreshToken, "Bearer");
    }

    public AuthResponse refresh(AuthRefreshRequest request) {
        String token = request.refreshToken();
        String tokenHash = hashToken(token);

        RefreshToken stored = refreshTokenRepository
                .findByTokenHashAndExpiresAtAfter(tokenHash, Instant.now())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid refresh token"));

        String accessToken = jwtService.generateToken(stored.getUserId());
        String newRefresh = rotateRefreshToken(stored);
        return new AuthResponse(accessToken, newRefresh, "Bearer");
    }

    public void logout() {
        UUID userId = currentUserService.requireUserId();
        refreshTokenRepository.deleteByUserId(userId);
    }

    private String normalizeEmail(String email) {
        return email.trim().toLowerCase(Locale.ROOT);
    }

    private String issueRefreshToken(UUID userId) {
        String token = generateTokenValue();
        String tokenHash = hashToken(token);
        Instant expiresAt = Instant.now().plus(refreshExpirationDays, ChronoUnit.DAYS);

        RefreshToken refreshToken = refreshTokenRepository.findByUserId(userId).orElse(new RefreshToken());
        refreshToken.setUserId(userId);
        refreshToken.setTokenHash(tokenHash);
        refreshToken.setExpiresAt(expiresAt);
        refreshTokenRepository.save(refreshToken);

        return token;
    }

    private String rotateRefreshToken(RefreshToken existing) {
        String token = generateTokenValue();
        existing.setTokenHash(hashToken(token));
        existing.setExpiresAt(Instant.now().plus(refreshExpirationDays, ChronoUnit.DAYS));
        refreshTokenRepository.save(existing);
        return token;
    }

    private String generateTokenValue() {
        byte[] bytes = new byte[32];
        SECURE_RANDOM.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }

    private String hashToken(String token) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(token.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder(hash.length * 2);
            for (byte b : hash) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Token hashing failed");
        }
    }
}
