package com.smarttruck.shared.security;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.smarttruck.shared.token.TokenBlacklist;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@ExtendWith(MockitoExtension.class)
class JwtTokenProviderTest {

    @Mock
    private TokenBlacklist tokenBlacklist;

    private JwtTokenProvider jwtTokenProvider;

    @BeforeEach
    void setUp() {
        // Use a known secret for unit tests (the previous in-code secret)
        jwtTokenProvider = new JwtTokenProvider("MinhaChaveSecretaMuitoLongaParaJWT1234567890");
        jwtTokenProvider.setTokenBlacklist(tokenBlacklist);
    }

    @Test
    void generateToken_shouldCreateValidToken() {
        // Arrange
        final String userId = "test-user-id";
        final String email = "test@example.com";

        // Act
        final String token = jwtTokenProvider.generateToken(userId, email, "Test User");

        // Assert
        assertNotNull(token);
        assertTrue(jwtTokenProvider.validateToken(token));
        assertEquals(email, jwtTokenProvider.getEmailFromToken(token));
    }

    @Test
    void validateToken_shouldReturnFalseForInvalidToken() {
        // Arrange
        final String invalidToken = "invalid.token.here";

        // Act & Assert
        assertFalse(jwtTokenProvider.validateToken(invalidToken));
    }

    @Test
    void validateToken_shouldReturnFalseForBlacklistedToken() {
        // Arrange
        final String userId = "test-user-id";
        final String email = "test@example.com";
        final String token = jwtTokenProvider.generateToken(userId, email, "Test User");
        when(tokenBlacklist.isBlacklisted(token)).thenReturn(true);

        // Act & Assert
        assertFalse(jwtTokenProvider.validateToken(token));
    }

    @Test
    void getEmailFromToken_shouldReturnCorrectEmail() {
        // Arrange
        final String userId = "test-user-id";
        final String email = "test@example.com";
        final String token = jwtTokenProvider.generateToken(userId, email, "Test User");

        // Act
        final String extractedEmail = jwtTokenProvider.getEmailFromToken(token);

        // Assert
        assertEquals(email, extractedEmail);
    }

    @Test
    void invalidateToken_shouldAddTokenToBlacklist() {
        // Arrange
        final String token = "test.token.here";

        // Act
        jwtTokenProvider.invalidateToken(token);

        // Assert
        verify(tokenBlacklist).addToBlacklist(token);
    }

    @Test
    void generatedToken_shouldHaveCorrectValidity() {
        // Arrange
        final String userId = "test-user-id";
        final String email = "test@example.com";

        // Act
        final String token = jwtTokenProvider.generateToken(userId, email, "Test User");

        // Assert
        final Claims claims = Jwts.parserBuilder()
                .setSigningKey(Keys
                        .hmacShaKeyFor("MinhaChaveSecretaMuitoLongaParaJWT1234567890".getBytes()))
                .build().parseClaimsJws(token).getBody();

        final long expirationTime = claims.getExpiration().getTime();
        final long issuedAtTime = claims.getIssuedAt().getTime();
        final long duration = expirationTime - issuedAtTime;

        assertEquals(JwtTokenProvider.ACCESS_TOKEN_VALIDITY_IN_MS, duration);
    }
}
