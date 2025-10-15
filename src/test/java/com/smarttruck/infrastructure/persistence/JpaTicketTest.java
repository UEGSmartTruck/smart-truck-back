package com.smarttruck.infrastructure.persistence;

import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class JpaTicketTest {

    @Test
    void shouldCreateInstanceWithDefaultConstructor() {
        // Act
        JpaTicket jpaTicket = new JpaTicket();

        // Assert
        assertNotNull(jpaTicket);
        assertNull(jpaTicket.getId());
        assertNull(jpaTicket.getCustomerId());
        assertNull(jpaTicket.getDescription());
        assertNull(jpaTicket.getStatus());
        assertNull(jpaTicket.getCreatedAt());
        assertNull(jpaTicket.getUpdatedAt());
        assertNull(jpaTicket.getDeletedAt());
    }

    @Test
    void shouldGenerateIdAndCreatedAtWhenNull() {
        // Act
        JpaTicket jpaTicket = new JpaTicket(
                null,
                "C123",
                "Motor failure",
                "OPEN",
                null,
                null,
                null);

        // Assert
        assertNotNull(jpaTicket.getId());
        assertDoesNotThrow(() -> UUID.fromString(jpaTicket.getId()), "ID should be a valid UUID");
        assertNotNull(jpaTicket.getCreatedAt());
        assertEquals("C123", jpaTicket.getCustomerId());
        assertEquals("Motor failure", jpaTicket.getDescription());
        assertEquals("OPEN", jpaTicket.getStatus());
        assertNull(jpaTicket.getUpdatedAt());
        assertNull(jpaTicket.getDeletedAt());
    }

    @Test
    void shouldUseProvidedValuesWhenNotNull() {
        // Arrange
        String id = UUID.randomUUID().toString();
        Instant now = Instant.now();

        // Act
        JpaTicket jpaTicket = new JpaTicket(
                id,
                "C999",
                "Engine overheating",
                "CLOSED",
                now,
                now,
                now);

        // Assert
        assertEquals(id, jpaTicket.getId());
        assertEquals("C999", jpaTicket.getCustomerId());
        assertEquals("Engine overheating", jpaTicket.getDescription());
        assertEquals("CLOSED", jpaTicket.getStatus());
        assertEquals(now, jpaTicket.getCreatedAt());
        assertEquals(now, jpaTicket.getUpdatedAt());
        assertEquals(now, jpaTicket.getDeletedAt());
    }

    @Test
    void shouldSetAndGetAllProperties() {
        // Arrange
        JpaTicket jpaTicket = new JpaTicket();
        Instant now = Instant.now();
        String id = UUID.randomUUID().toString();

        // Act
        jpaTicket.setId(id);
        jpaTicket.setCustomerId("C777");
        jpaTicket.setDescription("Brake system issue");
        jpaTicket.setStatus("IN_PROGRESS");
        jpaTicket.setCreatedAt(now);
        jpaTicket.setUpdatedAt(now);
        jpaTicket.setDeletedAt(now);

        // Assert
        assertEquals(id, jpaTicket.getId());
        assertEquals("C777", jpaTicket.getCustomerId());
        assertEquals("Brake system issue", jpaTicket.getDescription());
        assertEquals("IN_PROGRESS", jpaTicket.getStatus());
        assertEquals(now, jpaTicket.getCreatedAt());
        assertEquals(now, jpaTicket.getUpdatedAt());
        assertEquals(now, jpaTicket.getDeletedAt());
    }
}
