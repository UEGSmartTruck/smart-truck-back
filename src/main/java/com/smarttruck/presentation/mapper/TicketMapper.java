package com.smarttruck.presentation.mapper;

import com.smarttruck.domain.model.Ticket;
import com.smarttruck.presentation.dto.CreateTicketResponse;

/**
 * Mapeador simples entre a entidade de domínio {@link Ticket} e o DTO de
 * apresentação {@link CreateTicketResponse}.
 *
 * Implementado como utilitário estático sem estado.
 */
public final class TicketMapper {
    private TicketMapper() {
    }

    /**
     * Converte um ticket de domínio para o DTO de resposta.
     *
     * @param ticket ticket de domínio
     * @return DTO para resposta HTTP
     */
    public static CreateTicketResponse toResponse(Ticket ticket) {
        CreateTicketResponse r = new CreateTicketResponse();
        r.setId(ticket.getId());
        r.setCustomerId(ticket.getCustomerId());
        r.setDescription(ticket.getDescription());
        r.setStatus(ticket.getStatus().name());
        r.setCreatedAt(ticket.getCreatedAt());
        r.setUpdatedAt(ticket.getUpdatedAt());
        r.setDeletedAt(ticket.getDeletedAt());
        return r;
    }
}
