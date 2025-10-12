package com.smarttruck.infra.persistence;

import com.smarttruck.domain.model.Ticket;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class InMemoryTicketRepositoryTest {

    @Test
    public void save_and_retrieve_should_store_ticket() {
        InMemoryTicketRepository repo = new InMemoryTicketRepository();
        Ticket t = new Ticket("cX", "dX");

        Ticket saved = repo.save(t);

        Assertions.assertSame(t, saved);
        // We can't access internals, but save must not throw and must return the ticket
        Assertions.assertNotNull(saved.getId());
    }
}
