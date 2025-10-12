package com.smarttruck.presentation.mapper;

import com.smarttruck.domain.model.Ticket;
import com.smarttruck.domain.model.TicketStatus;
import com.smarttruck.presentation.dto.CreateTicketResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Instant;

public class TicketMapperTest {

    @Test
    public void toResponse_should_map_all_fields() {
        Instant created = Instant.now();
        Ticket t = new Ticket("id-ignored", "cust", "desc", created, TicketStatus.OPEN, null, null);

        CreateTicketResponse r = TicketMapper.toResponse(t);

        Assertions.assertEquals(t.getId(), r.getId());
        Assertions.assertEquals("cust", r.getCustomerId());
        Assertions.assertEquals("desc", r.getDescription());
        Assertions.assertEquals("OPEN", r.getStatus());
        Assertions.assertEquals(created, r.getCreatedAt());
    }
}
