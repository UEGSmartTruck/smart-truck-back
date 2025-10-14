package com.smarttruck.infrastructure.persistence;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;

/**
 * Entidade JPA que representa a tabela de tickets no banco de dados.
 *
 * Esta classe é usada internamente pelo adaptador Spring Data para mapear os
 * campos entre a camada de persistência e a entidade de domínio {@code Ticket}.
 */
@Entity
@Table(name = "tickets")
public class JpaTicket {

    @Id
    @Column(length = 36)
    private String id;

    @Column(name = "customer_id", nullable = false)
    private String customerId;

    @Column(nullable = false)
    private String description;

    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;

    @Column(name = "deleted_at")
    private Instant deletedAt;

    public JpaTicket() {
        // Construtor padrão exigido pelo JPA
    }

    /**
     * Construtor auxiliar utilizado pelo adaptador para criar a entidade a partir
     * do domínio.
     */
    public JpaTicket(String id, String customerId, String description, String status, Instant createdAt,
            Instant updatedAt, Instant deletedAt) {
        this.id = id == null ? UUID.randomUUID().toString() : id;
        this.customerId = customerId;
        this.description = description;
        this.status = status;
        this.createdAt = createdAt == null ? Instant.now() : createdAt;
        this.updatedAt = updatedAt;
        this.deletedAt = deletedAt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Instant getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(Instant deletedAt) {
        this.deletedAt = deletedAt;
    }
}
