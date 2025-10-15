package com.smarttruck.domain.factory;

import com.smarttruck.domain.model.Ticket;
import com.smarttruck.domain.model.TicketStatus;
import org.springframework.stereotype.Component;

/**
 * Fábrica que inicializa tickets que foram parcialmente solucionados por uma
 * IA.
 * O status inicial aplicado é {@link TicketStatus#IN_PROGRESS}.
 */
@Component
public class AiSolvedTicketFactory implements TicketFactory {

    @Override
    public Ticket create(String customerId, String description) {
        Ticket ticket = new Ticket(customerId, description);
        ticket.setStatus(TicketStatus.IN_PROGRESS);
        return ticket;
    }
}
