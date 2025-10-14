package com.smarttruck.domain.factory;

import com.smarttruck.domain.model.Ticket;
import com.smarttruck.domain.model.TicketStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class AiSolvedTicketFactoryTest {

    @Test
    public void createShouldReturnTicketInProgressAndSetUpdatedAt() {
        AiSolvedTicketFactory factory = new AiSolvedTicketFactory();

        Ticket ticket = factory.create("cust-1", "descr");

        Assertions.assertNotNull(ticket.getId());
        Assertions.assertEquals("cust-1", ticket.getCustomerId());
        Assertions.assertEquals("descr", ticket.getDescription());
        Assertions.assertEquals(TicketStatus.IN_PROGRESS, ticket.getStatus());
        Assertions.assertNotNull(ticket.getCreatedAt());
        Assertions.assertNotNull(ticket.getUpdatedAt(), "updatedAt should be set when status changed");
    }
}
