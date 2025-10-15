package com.smarttruck.infrastructure.persistence;

import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class JpaUserTest {

    @Test
    void defaultConstructor_shouldInitializeEmptyObject() {
        JpaUser user = new JpaUser();

        assertNull(user.getId());
        assertNull(user.getName());
        assertNull(user.getPhone());
        assertNull(user.getEmail());
        assertNull(user.getPasswordHash());
        assertNull(user.getCreatedAt());
        assertNull(user.getUpdatedAt());
        assertNull(user.getDeletedAt());
        assertNull(user.getLoginAt());
    }

    @Test
    void constructorWithAllFields_shouldInitializeFields() {
        String id = UUID.randomUUID().toString();
        String name = "Alice";
        String phone = "1234567890";
        String email = "alice@example.com";
        String passwordHash = "hashedPassword";
        Instant createdAt = Instant.now().minusSeconds(3600);
        Instant updatedAt = Instant.now().minusSeconds(1800);
        Instant deletedAt = Instant.now().minusSeconds(600);
        Instant loginAt = Instant.now();

        JpaUser user =
            new JpaUser(id, name, phone, email, passwordHash, createdAt, updatedAt, deletedAt,
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
    void constructorWithNullIdAndDates_shouldGenerateIdAndTimestamps() {
        String name = "Bob";
        String phone = "0987654321";
        String email = "bob@example.com";
        String passwordHash = "hashedPass";

        Instant before = Instant.now();
        JpaUser user = new JpaUser(null, name, phone, email, passwordHash, null, null, null, null);
        Instant after = Instant.now();

        assertNotNull(user.getId());
        assertDoesNotThrow(() -> UUID.fromString(user.getId()));

        assertNotNull(user.getCreatedAt());
        assertNotNull(user.getUpdatedAt());

        // createdAt e updatedAt estão entre before e after
        assertTrue(!user.getCreatedAt().isBefore(before) && !user.getCreatedAt().isAfter(after));
        assertTrue(!user.getUpdatedAt().isBefore(before) && !user.getUpdatedAt().isAfter(after));

        assertEquals(name, user.getName());
        assertEquals(phone, user.getPhone());
        assertEquals(email, user.getEmail());
        assertEquals(passwordHash, user.getPasswordHash());
        assertNull(user.getDeletedAt());
        assertNull(user.getLoginAt());
    }

    @Test
    void settersAndGetters_shouldWorkCorrectly() {
        JpaUser user = new JpaUser();

        String id = UUID.randomUUID().toString();
        String name = "Charlie";
        String phone = "5555555555";
        String email = "charlie@example.com";
        String passwordHash = "pass123";
        Instant createdAt = Instant.now().minusSeconds(1000);
        Instant updatedAt = Instant.now().minusSeconds(500);
        Instant deletedAt = Instant.now().minusSeconds(100);
        Instant loginAt = Instant.now();

        user.setId(id);
        user.setName(name);
        user.setPhone(phone);
        user.setEmail(email);
        user.setPasswordHash(passwordHash);
        user.setCreatedAt(createdAt);
        user.setUpdatedAt(updatedAt);
        user.setDeletedAt(deletedAt);
        user.setLoginAt(loginAt);

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
}
