package com.smarttruck.infra.persistence;

import com.smarttruck.domain.model.Ticket;
import com.smarttruck.domain.repository.TicketRepository;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Repositório em memória utilizado para desenvolvimento e testes rápidos.
 * Não oferece persistência entre execuções e não é recomendado para produção.
 */
@Repository
public class InMemoryTicketRepository implements TicketRepository {

    private final Map<String, Ticket> store = new ConcurrentHashMap<>();

    @Override
    public Ticket save(Ticket ticket) {
        store.put(ticket.getId(), ticket);
        return ticket;
    }
}
