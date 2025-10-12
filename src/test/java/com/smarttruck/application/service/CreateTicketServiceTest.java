package com.smarttruck.application.service;

import com.smarttruck.domain.factory.EscalatedTicketFactory;
import com.smarttruck.domain.model.Ticket;
import com.smarttruck.infra.persistence.InMemoryTicketRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class CreateTicketServiceTest {

    @Test
    public void create_ticket_when_ai_not_solved_should_be_escalated() {
        var repo = new InMemoryTicketRepository();
        var escalatedFactory = new EscalatedTicketFactory();
        var aiFactory = new com.smarttruck.domain.factory.AiSolvedTicketFactory();
        var service = new CreateTicketService(repo, aiFactory, escalatedFactory);

        Ticket ticket = service.create("customerId-1", "Problema no caminhão", false);

        Assertions.assertNotNull(ticket.getId());
        Assertions.assertEquals("customerId-1", ticket.getCustomerId());
        Assertions.assertEquals("Problema no caminhão", ticket.getDescription());
        Assertions.assertEquals("ESCALATED", ticket.getStatus().name());
    }
}
