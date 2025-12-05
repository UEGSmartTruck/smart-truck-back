package com.smarttruck.presentation.mapper;

import com.smarttruck.domain.model.Ticket;
import com.smarttruck.presentation.dto.CreateTicketResponse;

/**
 * Mapeador simples entre a entidade de domínio {@link Ticket} e o DTO de apresentação
 * {@link CreateTicketResponse}.
 * <p>
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
    public static CreateTicketResponse toResponse(final Ticket ticket) {
        return new CreateTicketResponse(ticket.getId(), ticket.getCustomerId(),
            ticket.getDescription(), ticket.getStatus().name(), ticket.getCreatedAt(),
            ticket.getUpdatedAt(), ticket.getDeletedAt());
    }
}
