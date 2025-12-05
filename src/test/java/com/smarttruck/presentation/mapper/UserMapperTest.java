package com.smarttruck.presentation.mapper;

import com.smarttruck.domain.model.User;
import com.smarttruck.presentation.dto.CreateUserResponse;
import com.smarttruck.presentation.dto.ListAllUserResponse;
import com.smarttruck.presentation.dto.UserData;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;

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

    @Test
    void toUserData_shouldMapAllFields() {
        // Arrange
        Instant now = Instant.now();
        User user = new User("123", "Alice", "1234567890", "alice@example.com", "hashedPass",
            now, now, null, now);

        // Act
        UserData userData = UserMapper.toUserData(user);

        // Assert
        assertEquals(user.getId(), userData.id());
        assertEquals(user.getName(), userData.name());
        assertEquals(user.getEmail(), userData.email());
        assertEquals(user.getPhone(), userData.phone());
        assertEquals(user.getCreatedAt(), userData.createdAt());
        assertEquals(user.getUpdatedAt(), userData.updatedAt());
        assertNull(userData.deletedAt());
        assertEquals(user.getLoginAt(), userData.loginAt());
    }

    @Test
    void toListAllResponse_shouldMapCorrectly() {
        // Arrange
        Instant now = Instant.now();
        User user1 = new User("1", "Alice", "111", "alice@example.com", "pass", now, now, null, now);
        User user2 = new User("2", "Bob", "222", "bob@example.com", "pass", now, now, null, now);
        User user3 = new User("3", "Charlie", "333", "charlie@example.com", "pass", now, now, null, now);

        List<User> users = Arrays.asList(user1, user2, user3);
        Page<User> page = new PageImpl<>(users, PageRequest.of(0, 20), 3);

        // Act
        ListAllUserResponse response = UserMapper.toListAllResponse(page);

        // Assert
        assertEquals(3, response.users().size());
        assertEquals(3, response.metadata().totalElements());
        assertEquals(1, response.metadata().totalPages());
        assertEquals(0, response.metadata().currentPage());
        assertEquals(20, response.metadata().pageSize());

        // Verify first user mapping
        UserData firstUser = response.users().get(0);
        assertEquals(user1.getId(), firstUser.id());
        assertEquals(user1.getName(), firstUser.name());
        assertEquals(user1.getEmail(), firstUser.email());
    }

}
