package com.smarttruck.infrastructure.persistence;

import org.springframework.stereotype.Repository;

import com.smarttruck.domain.model.Ticket;
import com.smarttruck.domain.repository.TicketRepository;

/**
 * Adaptador que usa Spring Data JPA para persistir tickets no banco relacional.
 *
 * Esta implementação converte entre a entidade de domínio {@link Ticket} e a
 * entidade JPA {@link JpaTicket} antes de delegar ao repositório JPA gerado
 * (`SpringDataJpaTicketRepository`).
 */
@Repository
public class JpaTicketRepository implements TicketRepository {
    private final SpringDataJpaTicketRepository repository;
    private final JpaTicketMapper mapper;

    public JpaTicketRepository(SpringDataJpaTicketRepository repository, JpaTicketMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public Ticket save(Ticket ticket) {
        JpaTicket entity = mapper.toEntity(ticket);
        if (entity == null) {
            throw new NullPointerException("Mapper returned null entity for ticket: " + ticket);
        }

        JpaTicket saved = repository.save(entity);
        return mapper.toDomain(saved);
    }
}
