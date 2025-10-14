package com.smarttruck.domain.factory;

import com.smarttruck.domain.model.Ticket;
import com.smarttruck.domain.model.TicketStatus;
import org.springframework.stereotype.Component;

/**
 * Fábrica que cria tickets no estado {@link TicketStatus#ESCALATED}, utilizado
 * quando o problema precisa ser encaminhado para atendimento humano ou
 * especializado.
 */
@Component
public class EscalatedTicketFactory implements TicketFactory {

    @Override
    public Ticket create(String customerId, String description) {
        Ticket ticket = new Ticket(customerId, description);
        ticket.setStatus(TicketStatus.ESCALATED);
        return ticket;
    }
}
