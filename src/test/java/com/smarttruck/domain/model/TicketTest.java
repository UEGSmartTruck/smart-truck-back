package com.smarttruck.domain.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Instant;

public class TicketTest {

    @Test
    public void constructor_and_status_updates_should_work() throws InterruptedException {
        Ticket t = new Ticket("c1", "d1");

        Assertions.assertEquals("c1", t.getCustomerId());
        Assertions.assertEquals("d1", t.getDescription());
        Assertions.assertNotNull(t.getCreatedAt());
        Assertions.assertNull(t.getUpdatedAt());

        Instant before = t.getCreatedAt();
        Thread.sleep(5);
        t.setStatus(TicketStatus.IN_PROGRESS);
        Assertions.assertNotNull(t.getUpdatedAt());
        Assertions.assertTrue(t.getUpdatedAt().isAfter(before));
    }
}
