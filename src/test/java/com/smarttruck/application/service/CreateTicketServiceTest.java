package com.smarttruck.application.service;

import com.smarttruck.domain.factory.AiSolvedTicketFactory;
import com.smarttruck.domain.factory.EscalatedTicketFactory;
import com.smarttruck.domain.model.Ticket;
import com.smarttruck.domain.repository.TicketRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.*;

class CreateTicketServiceTest {

    @Mock
    private TicketRepository ticketRepository;

    @Mock
    private AiSolvedTicketFactory aiSolvedTicketFactory;

    @Mock
    private EscalatedTicketFactory escalatedTicketFactory;

    @InjectMocks
    private CreateTicketService createTicketService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldUseAiSolvedTicketFactoryWhenAiSolvedIsTrue() {
        // Arrange
        String customerId = "123";
        String description = "AI solved issue";
        boolean aiSolved = true;

        Ticket ticket = new Ticket(customerId, description);
        Ticket savedTicket = new Ticket(customerId, description);

        when(aiSolvedTicketFactory.create(customerId, description)).thenReturn(ticket);
        when(ticketRepository.save(ticket)).thenReturn(savedTicket);

        // Act
        Ticket result = createTicketService.execute(customerId, description, aiSolved);

        // Assert
        verify(aiSolvedTicketFactory).create(customerId, description);
        verify(escalatedTicketFactory, never()).create(anyString(), anyString());
        verify(ticketRepository).save(ticket);
        assertEquals(savedTicket, result);
        assertEquals(customerId, result.getCustomerId());
        assertEquals(description, result.getDescription());
    }

    @Test
    void shouldUseEscalatedTicketFactoryWhenAiSolvedIsFalse() {
        // Arrange
        String customerId = "456";
        String description = "Manual escalation needed";
        boolean aiSolved = false;

        Ticket ticket = new Ticket(customerId, description);
        Ticket savedTicket = new Ticket(customerId, description);

        when(escalatedTicketFactory.create(customerId, description)).thenReturn(ticket);
        when(ticketRepository.save(ticket)).thenReturn(savedTicket);

        // Act
        Ticket result = createTicketService.execute(customerId, description, aiSolved);

        // Assert
        verify(escalatedTicketFactory).create(customerId, description);
        verify(aiSolvedTicketFactory, never()).create(anyString(), anyString());
        verify(ticketRepository).save(ticket);
        assertEquals(savedTicket, result);
        assertEquals(customerId, result.getCustomerId());
        assertEquals(description, result.getDescription());
    }

    @Test
    void shouldReturnSavedTicketFromRepository() {
        // Arrange
        String customerId = "789";
        String description = "Test case";
        boolean aiSolved = true;

        Ticket ticket = new Ticket(customerId, description);
        Ticket savedTicket = new Ticket(customerId, description);

        when(aiSolvedTicketFactory.create(customerId, description)).thenReturn(ticket);
        when(ticketRepository.save(ticket)).thenReturn(savedTicket);

        // Act
        Ticket result = createTicketService.execute(customerId, description, aiSolved);

        // Assert
        assertSame(savedTicket, result);
    }
}
