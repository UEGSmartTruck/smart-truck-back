package com.smarttruck.domain.model;

import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    void constructorWithoutId_shouldInitializeAllFields() {
        String name = "Alice";
        String phone = "1234567890";
        String email = "alice@example.com";
        String passwordHash = "hashedPassword";

        User user = new User(name, phone, email, passwordHash);

        // ID gerado automaticamente
        assertNotNull(user.getId());
        assertDoesNotThrow(() -> UUID.fromString(user.getId()));

        // Campos obrigatórios
        assertEquals(name, user.getName());
        assertEquals(phone, user.getPhone());
        assertEquals(email, user.getEmail());
        assertEquals(passwordHash, user.getPasswordHash());

        // Campos de tempo
        assertNotNull(user.getCreatedAt());
        assertNotNull(user.getUpdatedAt());
        assertNull(user.getDeletedAt());
        assertNull(user.getLoginAt());
    }

    @Test
    void constructorWithAllFields_shouldSetAllValuesCorrectly() {
        String id = UUID.randomUUID().toString();
        String name = "Bob";
        String phone = "0987654321";
        String email = "bob@example.com";
        String passwordHash = "hashedPassword";
        Instant createdAt = Instant.now().minusSeconds(3600);
        Instant updatedAt = Instant.now().minusSeconds(1800);
        Instant deletedAt = Instant.now().minusSeconds(600);
        Instant loginAt = Instant.now();

        User user = new User(id, name, phone, email, passwordHash, createdAt, updatedAt, deletedAt,
            loginAt);

        assertEquals(id, user.getId());
        assertEquals(name, user.getName());
        assertEquals(phone, user.getPhone());
        assertEquals(email, user.getEmail());
        assertEquals(passwordHash, user.getPasswordHash());
        assertEquals(createdAt, user.getCreatedAt());
        assertEquals(updatedAt, user.getUpdatedAt());
        assertEquals(deletedAt, user.getDeletedAt());
        assertEquals(loginAt, user.getLoginAt());
    }

    @Test
    void getters_shouldReturnExpectedValues() {
        String id = UUID.randomUUID().toString();
        String name = "Charlie";
        String phone = "5555555555";
        String email = "charlie@example.com";
        String passwordHash = "pass123";
        Instant createdAt = Instant.now();
        Instant updatedAt = Instant.now();
        Instant deletedAt = null;
        Instant loginAt = null;

        User user = new User(id, name, phone, email, passwordHash, createdAt, updatedAt, deletedAt,
            loginAt);

        assertEquals(id, user.getId());
        assertEquals(name, user.getName());
        assertEquals(phone, user.getPhone());
        assertEquals(email, user.getEmail());
        assertEquals(passwordHash, user.getPasswordHash());
        assertEquals(createdAt, user.getCreatedAt());
        assertEquals(updatedAt, user.getUpdatedAt());
        assertNull(user.getDeletedAt());
        assertNull(user.getLoginAt());
    }
}
