package com.smarttruck.infrastructure.persistence;

import org.springframework.stereotype.Component;

import com.smarttruck.domain.model.Ticket;
import com.smarttruck.domain.model.TicketStatus;

@Component
public class JpaTicketMapper {
    public Ticket toDomain(JpaTicket e) {
        if (e == null)
            return null;
        return new Ticket(
                e.getId(),
                e.getCustomerId(),
                e.getDescription(),
                TicketStatus.valueOf(e.getStatus()),
                e.getCreatedAt(),
                e.getUpdatedAt(),
                e.getDeletedAt());
    }

    public JpaTicket toEntity(Ticket t) {
        if (t == null)
            return null;
        return new JpaTicket(
                t.getId(),
                t.getCustomerId(),
                t.getDescription(),
                t.getStatus().name(),
                t.getCreatedAt(),
                t.getUpdatedAt(),
                t.getDeletedAt());
    }
}
