package com.smarttruck.presentation.controller;

import com.smarttruck.application.usecase.AuthenticateUserUseCase;
import com.smarttruck.application.usecase.RefreshTokenUseCase;
import com.smarttruck.domain.repository.RefreshTokenRepository;
import com.smarttruck.presentation.dto.*;
import com.smarttruck.presentation.mapper.RefreshTokenMapper;
import com.smarttruck.shared.security.JwtTokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class LoginController {

    private final AuthenticateUserUseCase authenticateUserUseCase;
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final RefreshTokenUseCase refreshTokenUseCase;

    public LoginController(final AuthenticateUserUseCase authenticateUserUseCase,
                           final JwtTokenProvider jwtTokenProvider,
                           final RefreshTokenRepository refreshTokenRepository,
                           final RefreshTokenUseCase refreshTokenUseCase) {
        this.authenticateUserUseCase = authenticateUserUseCase;
        this.jwtTokenProvider = jwtTokenProvider;
        this.refreshTokenRepository = refreshTokenRepository;
        this.refreshTokenUseCase = refreshTokenUseCase;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody final LoginRequest request) {
        try {
            final String accessToken =
                authenticateUserUseCase.execute(request.email(), request.password());

            // generate refresh token and persist mapping userId
            final String userId = jwtTokenProvider.getUserIdFromToken(accessToken);
            final String refreshToken = java.util.UUID.randomUUID().toString();
            refreshTokenRepository.save(refreshToken, userId);

            return ResponseEntity.ok(new RefreshTokenResponse(accessToken, refreshToken));
        } catch (final RuntimeException e) {
            // Usuário não encontrado ou senha inválida
            return ResponseEntity.status(401).body(new ErrorResponse(
                "Usuário ou senha inválidos",
                java.time.Instant.now(),
                new ErrorResponse.ErrorDetails("AUTH_FAILED", "Invalid credentials")
            ));
        }
    }

    @PostMapping("/refresh")
    public ResponseEntity<RefreshTokenResponse> refresh(
        @RequestBody final RefreshTokenRequest request) {
        try {
            final var token = refreshTokenUseCase.refresh(request.refreshToken());
            final var response = RefreshTokenMapper.toResponse(token);
            return ResponseEntity.ok(response);
        } catch (final Exception e) {
            return ResponseEntity.status(401).build();
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(final HttpServletRequest request) {
        final String token = extractToken(request);
        if (token != null) {
            jwtTokenProvider.invalidateToken(token);
            return ResponseEntity.ok().body(new MessageResponse("Logout realizado com sucesso"));
        }
        return ResponseEntity.status(401).body(new ErrorResponse(
            "Token de sessão expirado ou inválido",
            java.time.Instant.now(),
            new ErrorResponse.ErrorDetails("TOKEN_INVALID", "Token expired or invalid")
        ));
    }

    private String extractToken(final HttpServletRequest request) {
        final String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
