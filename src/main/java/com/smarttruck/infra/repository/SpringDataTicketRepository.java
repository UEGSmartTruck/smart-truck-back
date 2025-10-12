package com.smarttruck.infra.repository;

import com.smarttruck.domain.model.Ticket;
import com.smarttruck.domain.model.TicketStatus;
import com.smarttruck.domain.repository.TicketRepository;
import com.smarttruck.infra.entity.TicketEntity;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

/**
 * Adaptador que usa Spring Data JPA para persistir tickets no banco relacional.
 *
 * Esta implementação converte entre a entidade de domínio {@link Ticket} e a
 * entidade JPA {@link TicketEntity} antes de delegar ao repositório JPA gerado
 * (`JpaTicketRepository`).
 */
@Repository
@Primary
public class SpringDataTicketRepository implements TicketRepository {

    private final JpaTicketRepository jpa;

    public SpringDataTicketRepository(JpaTicketRepository jpa) {
        this.jpa = jpa;
    }

    @Override
    public Ticket save(Ticket ticket) {
        TicketEntity e = new TicketEntity(
                ticket.getId(),
                ticket.getCustomerId(),
                ticket.getDescription(),
                ticket.getStatus().name(),
                ticket.getCreatedAt(),
                ticket.getUpdatedAt(),
                ticket.getDeletedAt());

        e = jpa.save(e);

        Ticket t = new Ticket(e.getId(), e.getCustomerId(), e.getDescription(), e.getCreatedAt(),
                TicketStatus.valueOf(e.getStatus()), e.getUpdatedAt(), e.getDeletedAt());
        return t;
    }
}
