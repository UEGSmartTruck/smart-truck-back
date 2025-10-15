package com.smarttruck.infrastructure.persistence;

import com.smarttruck.domain.model.Ticket;
import com.smarttruck.domain.model.TicketStatus;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class JpaTicketMapperTest {

    private final JpaTicketMapper mapper = new JpaTicketMapper();

    @Test
    void shouldReturnNullWhenToDomainReceivesNull() {
        assertNull(mapper.toDomain(null));
    }

    @Test
    void shouldReturnNullWhenToEntityReceivesNull() {
        assertNull(mapper.toEntity(null));
    }

    @Test
    void shouldMapJpaTicketToDomainTicket() {
        // Arrange
        String id = UUID.randomUUID().toString();
        Instant now = Instant.now();
        JpaTicket jpaTicket = new JpaTicket(id, "C001", "Engine failure", "OPEN", now, now, null);

        // Act
        Ticket domain = mapper.toDomain(jpaTicket);

        // Assert
        assertNotNull(domain);
        assertEquals(id, domain.getId());
        assertEquals("C001", domain.getCustomerId());
        assertEquals("Engine failure", domain.getDescription());
        assertEquals(TicketStatus.OPEN, domain.getStatus());
        assertEquals(now, domain.getCreatedAt());
        assertEquals(now, domain.getUpdatedAt());
        assertNull(domain.getDeletedAt());
    }

    @Test
    void shouldMapDomainTicketToJpaTicket() {
        // Arrange
        String id = UUID.randomUUID().toString();
        Instant now = Instant.now();
        Ticket domain =
            new Ticket(id, "C002", "Brake issue", TicketStatus.RESOLVED, now, now, null);

        // Act
        JpaTicket jpaTicket = mapper.toEntity(domain);

        // Assert
        assertNotNull(jpaTicket);
        assertEquals(id, jpaTicket.getId());
        assertEquals("C002", jpaTicket.getCustomerId());
        assertEquals("Brake issue", jpaTicket.getDescription());
        assertEquals("RESOLVED", jpaTicket.getStatus());
        assertEquals(now, jpaTicket.getCreatedAt());
        assertEquals(now, jpaTicket.getUpdatedAt());
        assertNull(jpaTicket.getDeletedAt());
    }

    @Test
    void shouldMapStatusCorrectlyInBothDirections() {
        // Arrange
        Instant now = Instant.now();

        Ticket domain = new Ticket(UUID.randomUUID().toString(), "C003", "Sensor fault",
            TicketStatus.IN_PROGRESS, now, null, null);

        // Act
        JpaTicket entity = mapper.toEntity(domain);
        Ticket mappedBack = mapper.toDomain(entity);

        // Assert
        assertEquals(TicketStatus.IN_PROGRESS, mappedBack.getStatus());
        assertEquals(domain.getStatus().name(), entity.getStatus());
    }
}
