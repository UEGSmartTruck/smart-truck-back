package com.smarttruck.presentation.dto;

import jakarta.validation.constraints.NotBlank;

import java.time.Instant;

/**
 * DTO de resposta após criação de um ticket.
 * Contém campos essenciais que o cliente da API pode usar para rastrear o
 * ticket.
 */
public class CreateTicketResponse {
    private String id;
    @NotBlank
    private String customerId;
    @NotBlank
    private String description;
    private String status;
    private Instant createdAt;
    private Instant updatedAt;
    private Instant deletedAt;

    public CreateTicketResponse() {
    }

    public CreateTicketResponse(String id, String customerId, String description, String status,
                                Instant createdAt) {
        this.id = id;
        this.customerId = customerId;
        this.description = description;
        this.status = status;
        this.createdAt = createdAt;
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
