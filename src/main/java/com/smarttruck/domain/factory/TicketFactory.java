package com.smarttruck.domain.factory;

import com.smarttruck.domain.model.Ticket;

/**
 * Fábrica para criação de {@link Ticket} com diferentes políticas de
 * inicialização de estado.
 * <p>
 * Implementações concretas definem o status inicial do ticket (ex.: AI-solved
 * ou escalado).
 */
public interface TicketFactory {
    /**
     * Cria um novo ticket aplicando a política de fábrica.
     *
     * @param customerId  identificador do cliente
     * @param description descrição do ticket
     * @return instância criada de {@link Ticket}
     */
    Ticket create(String customerId, String description);
}
