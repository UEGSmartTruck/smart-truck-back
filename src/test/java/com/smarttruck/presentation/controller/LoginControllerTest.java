package com.smarttruck.presentation.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import com.smarttruck.application.usecase.AuthenticateUserUseCase;
import com.smarttruck.presentation.dto.ErrorResponse;
import com.smarttruck.presentation.dto.LoginRequest;
import com.smarttruck.presentation.dto.MessageResponse;
import com.smarttruck.presentation.dto.TokenResponse;
import com.smarttruck.shared.security.JwtTokenProvider;

@ExtendWith(MockitoExtension.class)
class LoginControllerTest {

    @Mock
    private AuthenticateUserUseCase authenticateUserUseCase;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    private LoginController loginController;

    @BeforeEach
    void setUp() {
        loginController = new LoginController(authenticateUserUseCase, jwtTokenProvider);
    }

    @Test
    void login_shouldReturnTokenResponse() {
        // Arrange
        final String email = "test@example.com";
        final String password = "password";
        final String token = "test.token.here";
        final LoginRequest request = new LoginRequest(email, password);

        when(authenticateUserUseCase.execute(email, password)).thenReturn(token);

        // Act
        final ResponseEntity<?> response = loginController.login(request);

        // Assert
        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertTrue(response.getBody() instanceof TokenResponse);
        assertEquals(token, ((TokenResponse) response.getBody()).accessToken());
        assertEquals(JwtTokenProvider.ACCESS_TOKEN_VALIDITY_IN_MS / 1000,
                ((TokenResponse) response.getBody()).expiresIn());

        verify(authenticateUserUseCase).execute(email, password);
    }

    @Test
    void logout_shouldInvalidateToken() {
        // Arrange
        final String token = "test.token.here";
        final MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer " + token);

        // Act
        final ResponseEntity<?> response = loginController.logout(request);

        // Assert
        assertEquals(200, response.getStatusCode().value());
        assertTrue(response.getBody() instanceof MessageResponse);
        verify(jwtTokenProvider).invalidateToken(token);
    }

    @Test
    void logout_shouldHandleMissingToken() {
        // Arrange
        final MockHttpServletRequest request = new MockHttpServletRequest();

        // Act
        final ResponseEntity<?> response = loginController.logout(request);

        // Assert
        assertEquals(401, response.getStatusCode().value());
        assertTrue(response.getBody() instanceof ErrorResponse);
        verify(jwtTokenProvider, never()).invalidateToken(any());
    }

    @Test
    void logout_shouldHandleInvalidAuthorizationHeader() {
        // Arrange
        final MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Invalid format");

        // Act
        final ResponseEntity<?> response = loginController.logout(request);

        // Assert
        assertEquals(401, response.getStatusCode().value());
        assertTrue(response.getBody() instanceof ErrorResponse);
        verify(jwtTokenProvider, never()).invalidateToken(any());
    }
}
