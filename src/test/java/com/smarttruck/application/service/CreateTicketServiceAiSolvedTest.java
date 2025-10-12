package com.smarttruck.application.service;

import com.smarttruck.domain.factory.AiSolvedTicketFactory;
import com.smarttruck.domain.factory.EscalatedTicketFactory;
import com.smarttruck.domain.model.Ticket;
import com.smarttruck.infra.persistence.InMemoryTicketRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class CreateTicketServiceAiSolvedTest {

    @Test
    public void create_ticket_when_ai_solved_should_be_in_progress() {
        var repo = new InMemoryTicketRepository();
        var escalatedFactory = new EscalatedTicketFactory();
        var aiFactory = new AiSolvedTicketFactory();
        var service = new CreateTicketService(repo, aiFactory, escalatedFactory);

        Ticket ticket = service.create("customer-2", "Descrição X", true);

        Assertions.assertEquals("customer-2", ticket.getCustomerId());
        Assertions.assertEquals("Descrição X", ticket.getDescription());
        Assertions.assertEquals("IN_PROGRESS", ticket.getStatus().name());
    }
}
