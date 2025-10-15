package com.smarttruck.domain.model;

/**
 * Enum que representa os estados possíveis de um {@link Ticket}.
 */
public enum TicketStatus {
    /**
     * Ticket recém-criado, aguardando processamento.
     */
    OPEN,

    /**
     * Ticket que está sendo processado (ex.: solução realizada pela IA em
     * andamento).
     */
    IN_PROGRESS,

    /**
     * Ticket que foi escalado para atendimento humano/especializado.
     */
    ESCALATED,

    /**
     * Ticket resolvido/fechado.
     */
    RESOLVED
}
