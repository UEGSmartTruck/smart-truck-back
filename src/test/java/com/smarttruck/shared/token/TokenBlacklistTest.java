package com.smarttruck.shared.token;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TokenBlacklistTest {

    private TokenBlacklist tokenBlacklist;

    @BeforeEach
    void setUp() {
        tokenBlacklist = new TokenBlacklist();
    }

    @Test
    void addToBlacklist_shouldAddToken() {
        // Arrange
        final String token = "test-token";

        // Act
        tokenBlacklist.addToBlacklist(token);

        // Assert
        assertTrue(tokenBlacklist.isBlacklisted(token));
    }

    @Test
    void isBlacklisted_shouldReturnFalseForNonBlacklistedToken() {
        // Arrange
        final String token = "non-blacklisted-token";

        // Assert
        assertFalse(tokenBlacklist.isBlacklisted(token));
    }

    @Test
    void removeFromBlacklist_shouldRemoveToken() {
        // Arrange
        final String token = "test-token";
        tokenBlacklist.addToBlacklist(token);

        // Act
        tokenBlacklist.removeFromBlacklist(token);

        // Assert
        assertFalse(tokenBlacklist.isBlacklisted(token));
    }

    @Test
    void removeFromBlacklist_shouldHandleNonExistentToken() {
        // Arrange
        final String token = "non-existent-token";

        // Act & Assert
        assertDoesNotThrow(() -> tokenBlacklist.removeFromBlacklist(token));
    }
}
