package com.smarttruck.presentation.mapper;

import static org.junit.jupiter.api.Assertions.*;

import com.smarttruck.domain.model.Ticket;
import com.smarttruck.domain.model.TicketStatus;
import com.smarttruck.presentation.dto.CreateTicketResponse;
import org.junit.jupiter.api.Test;

import java.time.Instant;

class TicketMapperTest {

    @Test
    void shouldMapDomainTicketToResponseCorrectly() {
        // Arrange
        Instant now = Instant.now();
        Ticket ticket = new Ticket(
                "1", // id
                "customer-123", // customerId
                "Problema no caminhão", // description
                TicketStatus.OPEN, // status
                now, // createdAt
                now, // updatedAt
                null // deletedAt
        );

        // Act
        CreateTicketResponse response = TicketMapper.toResponse(ticket);

        // Assert
        assertEquals(ticket.getId(), response.getId());
        assertEquals(ticket.getCustomerId(), response.getCustomerId());
        assertEquals(ticket.getDescription(), response.getDescription());
        assertEquals(ticket.getStatus().name(), response.getStatus());
        assertEquals(ticket.getCreatedAt(), response.getCreatedAt());
        assertEquals(ticket.getUpdatedAt(), response.getUpdatedAt());
        assertEquals(ticket.getDeletedAt(), response.getDeletedAt());
    }

    @Test
    void shouldThrowNullPointerExceptionWhenTicketIsNull() {
        // Act & Assert
        assertThrows(NullPointerException.class, () -> TicketMapper.toResponse(null));
    }
}
