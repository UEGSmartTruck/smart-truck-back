package com.smarttruck.presentation.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import java.time.Instant;
import org.junit.jupiter.api.Test;
import com.smarttruck.domain.model.User;
import com.smarttruck.presentation.dto.CreateUserResponse;

class UserMapperTest {

    @Test
    void toResponse_shouldMapAllFields() {
        // Arrange
        final Instant now = Instant.now();
        final User user = new User("123", "Alice", "1234567890", "alice@example.com", "hashedPass", now,
                now, now, now);

        // Act
        final CreateUserResponse response = UserMapper.toResponse(user);

        // Assert
        assertEquals(user.getId(), response.id());
        assertEquals(user.getName(), response.name());
        assertEquals(user.getEmail(), response.email());
        assertEquals(user.getPhone(), response.phone());
        assertEquals(user.getCreatedAt(), response.createdAt());
        assertEquals(user.getUpdatedAt(), response.updatedAt());
        assertEquals(user.getDeletedAt(), response.deletedAt());
        assertEquals(user.getLoginAt(), response.loginAt());
    }

    @Test
    void toResponse_shouldHandleNullOptionalFields() {
        // Arrange
        final Instant now = Instant.now();
        final User user = new User("123", "Bob", "0987654321", "bob@example.com", "hashedPass", now, now,
                null,
                // deletedAt
                null // loginAt
        );

        // Act
        final CreateUserResponse response = UserMapper.toResponse(user);

        // Assert
        assertEquals(user.getId(), response.id());
        assertEquals(user.getName(), response.name());
        assertEquals(user.getEmail(), response.email());
        assertEquals(user.getPhone(), response.phone());
        assertEquals(user.getCreatedAt(), response.createdAt());
        assertEquals(user.getUpdatedAt(), response.updatedAt());
        assertNull(response.deletedAt());
        assertNull(response.loginAt());
    }

}
