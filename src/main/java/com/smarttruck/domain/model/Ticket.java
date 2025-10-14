package com.smarttruck.domain.model;

import java.time.Instant;
import java.util.UUID;

/**
 * Representa um ticket de atendimento no domínio da aplicação.
 * <p>
 * Esta entidade contém informações imutáveis como `id`, `customerId`,
 * `description` e `createdAt` e campos mutáveis que representam o estado do
 * ticket (`status`, `updatedAt`, `deletedAt`). A classe é intencionalmente
 * simples e contém lógica mínima: ao alterar o status via
 * {@link #setStatus(TicketStatus)} o campo {@code updatedAt} é atualizado
 * automaticamente.
 * </p>
 */
public class Ticket {
    private final String id;
    private final String customerId;
    private final String description;
    private TicketStatus status;
    private final Instant createdAt;
    private Instant updatedAt;
    private Instant deletedAt;

    /**
     * Cria um novo ticket com um id gerado e timestamp de criação atual. O status
     * inicial é {@link TicketStatus#OPEN}.
     *
     * @param customerId  identificador do cliente/condutor responsável pelo ticket
     * @param description descrição textual do problema
     */
    public Ticket(String customerId, String description) {
        this.id = UUID.randomUUID().toString();
        this.customerId = customerId;
        this.description = description;
        this.status = TicketStatus.OPEN;
        this.createdAt = Instant.now();
        this.updatedAt = null;
        this.deletedAt = null;
    }

    /**
     * Construtor completo usado para reconstrução a partir de persistência ou
     * fixtures de teste.
     *
     * @param id          identificador do ticket
     * @param customerId  identificador do cliente
     * @param description descrição do ticket
     * @param status      status atual do ticket
     * @param createdAt   instante de criação
     * @param updatedAt   instante da última atualização (pode ser {@code null})
     * @param deletedAt   instante de exclusão lógica (pode ser {@code null})
     */
    public Ticket(String id, String customerId, String description, TicketStatus status, Instant createdAt,
            Instant updatedAt, Instant deletedAt) {
        this.id = id;
        this.customerId = customerId;
        this.description = description;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deletedAt = deletedAt;
    }

    public String getId() {
        return id;
    }

    public String getCustomerId() {
        return customerId;
    }

    public String getDescription() {
        return description;
    }

    public TicketStatus getStatus() {
        return status;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public Instant getDeletedAt() {
        return deletedAt;
    }

    /**
     * Atualiza o status do ticket e ajusta o timestamp {@code updatedAt} para o
     * instante atual.
     *
     * @param status novo status do ticket
     */
    public void setStatus(TicketStatus status) {
        this.status = status;
        this.updatedAt = Instant.now();
    }

    /**
     * Define explicitamente o instante da última atualização. Usado para
     * reconstrução a partir de persistência ou em testes.
     *
     * @param updatedAt instante da última atualização
     */
    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    /**
     * Marca o ticket como removido logicamente definindo {@code deletedAt}.
     *
     * @param deletedAt instante da exclusão lógica
     */
    public void setDeletedAt(Instant deletedAt) {
        this.deletedAt = deletedAt;
    }
}
