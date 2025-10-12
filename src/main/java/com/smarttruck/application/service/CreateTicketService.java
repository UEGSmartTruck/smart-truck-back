package com.smarttruck.application.service;

import com.smarttruck.domain.factory.AiSolvedTicketFactory;
import com.smarttruck.domain.factory.EscalatedTicketFactory;
import com.smarttruck.domain.factory.TicketFactory;
import com.smarttruck.domain.model.Ticket;
import com.smarttruck.domain.repository.TicketRepository;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Serviço de aplicação responsável pela criação de tickets. Este serviço
 * implementa o
 * caso de uso {@link CreateTicketUseCase} e delega a criação inicial para uma
 * fábrica
 * dependendo se o ticket foi (parcialmente) resolvido por uma IA ou precisa ser
 * escalado.
 *
 * Inputs: customerId, description, aiSolved flag.
 * Output: Ticket persistido com id e timestamps.
 */
@Service
public class CreateTicketService implements CreateTicketUseCase {

    private final TicketRepository ticketRepository;
    private final Map<Boolean, TicketFactory> factoryByAiSolved;

    public CreateTicketService(TicketRepository ticketRepository,
            AiSolvedTicketFactory aiSolvedTicketFactory,
            EscalatedTicketFactory escalatedTicketFactory) {
        this.ticketRepository = ticketRepository;
        this.factoryByAiSolved = Map.of(
                true, aiSolvedTicketFactory,
                false, escalatedTicketFactory);
    }

    /**
     * Cria e persiste um ticket.
     *
     * @param customerId  identificador do cliente
     * @param description descrição do ticket
     * @param aiSolved    flag que indica se a IA já resolveu parcialmente o
     *                    problema
     * @return ticket persistido com id e timestamps preenchidos
     */
    @Override
    public Ticket create(String customerId, String description, boolean aiSolved) {
        TicketFactory factory = factoryByAiSolved.get(aiSolved);
        Ticket ticket = factory.create(customerId, description);
        return ticketRepository.save(ticket);
    }
}
