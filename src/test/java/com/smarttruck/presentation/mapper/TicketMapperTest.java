package com.smarttruck.presentation.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import java.time.Instant;
import org.junit.jupiter.api.Test;
import com.smarttruck.domain.model.Ticket;
import com.smarttruck.domain.model.TicketStatus;
import com.smarttruck.presentation.dto.CreateTicketResponse;

class TicketMapperTest {

    @Test
    void shouldMapDomainTicketToResponseCorrectly() {
        // Arrange
        final Instant now = Instant.now();
        final Ticket ticket = new Ticket("1", // id
                "customer-123", // customerId
                "Problema no caminhão", // description
                TicketStatus.OPEN, // status
                now, // createdAt
                now, // updatedAt
                null // deletedAt
        );

        // Act
        final CreateTicketResponse response = TicketMapper.toResponse(ticket);

        // Assert
        assertEquals(ticket.getId(), response.id());
        assertEquals(ticket.getCustomerId(), response.customerId());
        assertEquals(ticket.getDescription(), response.description());
        assertEquals(ticket.getStatus().name(), response.status());
        assertEquals(ticket.getCreatedAt(), response.createdAt());
        assertEquals(ticket.getUpdatedAt(), response.updatedAt());
        assertEquals(ticket.getDeletedAt(), response.deletedAt());
    }

    @Test
    void shouldThrowNullPointerExceptionWhenTicketIsNull() {
        // Act & Assert
        assertThrows(NullPointerException.class, () -> TicketMapper.toResponse(null));
    }
}
