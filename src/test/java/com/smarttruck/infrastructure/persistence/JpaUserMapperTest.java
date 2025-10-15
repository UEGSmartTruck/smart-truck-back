package com.smarttruck.infrastructure.persistence;

import com.smarttruck.domain.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class JpaUserMapperTest {

    private JpaUserMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new JpaUserMapper();
    }

    @Test
    void toDomain_shouldReturnNullWhenInputIsNull() {
        User result = mapper.toDomain(null);
        assertNull(result);
    }

    @Test
    void toDomain_shouldMapAllFieldsCorrectly() {
        String id = UUID.randomUUID().toString();
        String name = "Alice";
        String phone = "1234567890";
        String email = "alice@example.com";
        String passwordHash = "hashedPassword";
        Instant createdAt = Instant.now().minusSeconds(3600);
        Instant updatedAt = Instant.now().minusSeconds(1800);
        Instant deletedAt = Instant.now().minusSeconds(600);
        Instant loginAt = Instant.now();

        JpaUser entity =
            new JpaUser(id, name, phone, email, passwordHash, createdAt, updatedAt, deletedAt,
                loginAt);

        User domain = mapper.toDomain(entity);

        assertNotNull(domain);
        assertEquals(id, domain.getId());
        assertEquals(name, domain.getName());
        assertEquals(phone, domain.getPhone());
        assertEquals(email, domain.getEmail());
        assertEquals(passwordHash, domain.getPasswordHash());
        assertEquals(createdAt, domain.getCreatedAt());
        assertEquals(updatedAt, domain.getUpdatedAt());
        assertEquals(deletedAt, domain.getDeletedAt());
        assertEquals(loginAt, domain.getLoginAt());
    }

    @Test
    void toEntity_shouldReturnNullWhenInputIsNull() {
        JpaUser result = mapper.toEntity(null);
        assertNull(result);
    }

    @Test
    void toEntity_shouldMapAllFieldsCorrectly() {
        String id = UUID.randomUUID().toString();
        String name = "Bob";
        String phone = "0987654321";
        String email = "bob@example.com";
        String passwordHash = "hashedPass";
        Instant createdAt = Instant.now().minusSeconds(3600);
        Instant updatedAt = Instant.now().minusSeconds(1800);
        Instant deletedAt = Instant.now().minusSeconds(600);
        Instant loginAt = Instant.now();

        User domain =
            new User(id, name, phone, email, passwordHash, createdAt, updatedAt, deletedAt,
                loginAt);

        JpaUser entity = mapper.toEntity(domain);

        assertNotNull(entity);
        assertEquals(id, entity.getId());
        assertEquals(name, entity.getName());
        assertEquals(phone, entity.getPhone());
        assertEquals(email, entity.getEmail());
        assertEquals(passwordHash, entity.getPasswordHash());
        assertEquals(createdAt, entity.getCreatedAt());
        assertEquals(updatedAt, entity.getUpdatedAt());
        assertEquals(deletedAt, entity.getDeletedAt());
        assertEquals(loginAt, entity.getLoginAt());
    }
}
