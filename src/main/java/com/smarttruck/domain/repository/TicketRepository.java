package com.smarttruck.domain.repository;

import com.smarttruck.domain.model.Ticket;

/**
 * Porta de repositório para persistência de {@link Ticket} no domínio.
 *
 * Implementações podem ser em memória (desenvolvimento) ou baseada em JPA para
 * produção.
 */
public interface TicketRepository {
    /**
     * Persiste o ticket e retorna a instância persistida (com possíveis alterações
     * de id/timestamps).
     *
     * @param ticket ticket a persistir
     * @return ticket persistido
     */
    Ticket save(Ticket ticket);
}
