package com.smarttruck.domain.factory;

import com.smarttruck.domain.model.Ticket;
import com.smarttruck.domain.model.TicketStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class EscalatedTicketFactoryTest {

    @Test
    public void createShouldReturnEscalatedTicket() {
        EscalatedTicketFactory factory = new EscalatedTicketFactory();

        Ticket ticket = factory.create("cust-2", "descr2");

        Assertions.assertNotNull(ticket.getId());
        Assertions.assertEquals("cust-2", ticket.getCustomerId());
        Assertions.assertEquals("descr2", ticket.getDescription());
        Assertions.assertEquals(TicketStatus.ESCALATED, ticket.getStatus());
    }
}
