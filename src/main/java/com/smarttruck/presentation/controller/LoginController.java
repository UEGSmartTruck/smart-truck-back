
package com.smarttruck.presentation.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.smarttruck.application.usecase.AuthenticateUserUseCase;
import com.smarttruck.presentation.dto.ErrorResponse;
import com.smarttruck.presentation.dto.LoginRequest;
import com.smarttruck.presentation.dto.MessageResponse;
import com.smarttruck.presentation.dto.TokenResponse;
import com.smarttruck.shared.security.JwtTokenProvider;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/auth")
public class LoginController {

    private final AuthenticateUserUseCase authenticateUserUseCase;
    private final JwtTokenProvider jwtTokenProvider;

    public LoginController(final AuthenticateUserUseCase authenticateUserUseCase,
            final JwtTokenProvider jwtTokenProvider) {
        this.authenticateUserUseCase = authenticateUserUseCase;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody final LoginRequest request) {
        try {
            final String accessToken =
                    authenticateUserUseCase.execute(request.email(), request.password());
            return ResponseEntity.ok(new TokenResponse(accessToken,
                    JwtTokenProvider.ACCESS_TOKEN_VALIDITY_IN_MS / 1000));
        } catch (final RuntimeException e) {
            // Usuário não encontrado ou senha inválida
            return ResponseEntity.status(401).body(new ErrorResponse("Usuário ou senha inválidos"));
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(final HttpServletRequest request) {
        final String token = extractToken(request);
        if (token != null) {
            jwtTokenProvider.invalidateToken(token);
            return ResponseEntity.ok().body(new MessageResponse("Logout realizado com sucesso"));
        }
        return ResponseEntity.status(401)
                .body(new ErrorResponse("Token de sessão expirado ou inválido"));
    }

    private String extractToken(final HttpServletRequest request) {
        final String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
