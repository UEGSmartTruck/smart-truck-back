package com.smarttruck.presentation.mapper;

import com.smarttruck.domain.model.User;
import com.smarttruck.presentation.dto.CreateUserResponse;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class UserMapperTest {

    @Test
    void toResponse_shouldMapAllFields() {
        // Arrange
        Instant now = Instant.now();
        User user =
            new User("123", "Alice", "1234567890", "alice@example.com", "hashedPass", now, now, now,
                now);

        // Act
        CreateUserResponse response = UserMapper.toResponse(user);

        // Assert
        assertEquals(user.getId(), response.getId());
        assertEquals(user.getName(), response.getName());
        assertEquals(user.getEmail(), response.getEmail());
        assertEquals(user.getPhone(), response.getPhone());
        assertEquals(user.getCreatedAt(), response.getCreatedAt());
        assertEquals(user.getUpdatedAt(), response.getUpdatedAt());
        assertEquals(user.getDeletedAt(), response.getDeletedAt());
        assertEquals(user.getLoginAt(), response.getLoginAt());
    }

    @Test
    void toResponse_shouldHandleNullOptionalFields() {
        // Arrange
        Instant now = Instant.now();
        User user =
            new User("123", "Bob", "0987654321", "bob@example.com", "hashedPass", now, now, null,
                // deletedAt
                null  // loginAt
            );

        // Act
        CreateUserResponse response = UserMapper.toResponse(user);

        // Assert
        assertEquals(user.getId(), response.getId());
        assertEquals(user.getName(), response.getName());
        assertEquals(user.getEmail(), response.getEmail());
        assertEquals(user.getPhone(), response.getPhone());
        assertEquals(user.getCreatedAt(), response.getCreatedAt());
        assertEquals(user.getUpdatedAt(), response.getUpdatedAt());
        assertNull(response.getDeletedAt());
        assertNull(response.getLoginAt());
    }

}
