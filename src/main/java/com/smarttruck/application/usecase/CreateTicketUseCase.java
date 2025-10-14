package com.smarttruck.application.usecase;

import com.smarttruck.domain.model.Ticket;

/**
 * Caso de uso de criação de ticket (porta da camada de aplicação).
 */
public interface CreateTicketUseCase {
    /**
     * Cria um ticket com base nos parâmetros fornecidos.
     *
     * @param customerId  identificador do cliente
     * @param description descrição do ticket
     * @param aiSolved    indica se a IA já resolveu parcialmente o problema
     * @return ticket criado e persistido
     */
    Ticket create(String customerId, String description, boolean aiSolved);
}
