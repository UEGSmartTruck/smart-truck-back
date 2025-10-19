package com.smarttruck.shared.security;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import com.smarttruck.shared.token.TokenBlacklist;

@SpringBootTest
@ActiveProfiles("test")
class JwtTokenProviderIntegrationTest {

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private TokenBlacklist tokenBlacklist;

    @Test
    void tokenLifecycle_shouldWorkCorrectly() {
        // Arrange
        final String userId = "test-user-id";
        final String email = "test@example.com";

        // Act & Assert - Token Generation
        final String token = jwtTokenProvider.generateToken(userId, email, "Integration User");
        assertNotNull(token);
        assertTrue(jwtTokenProvider.validateToken(token));
        // Act & Assert - Token Validation
        final String extractedEmail = jwtTokenProvider.getEmailFromToken(token);
        assertEquals(email, extractedEmail);

        // Act & Assert - Token Invalidation
        jwtTokenProvider.invalidateToken(token);
        assertFalse(jwtTokenProvider.validateToken(token));
    }

    @Test
    void tokenBlacklist_shouldPersistBetweenRequests() {
        // Arrange
        final String userId = "another-user-id";
        final String email = "another@example.com";
        final String token = jwtTokenProvider.generateToken(userId, email, "Integration User");

        // Act
        jwtTokenProvider.invalidateToken(token);

        // Assert
        assertFalse(jwtTokenProvider.validateToken(token));
        assertTrue(tokenBlacklist.isBlacklisted(token));
    }
}
