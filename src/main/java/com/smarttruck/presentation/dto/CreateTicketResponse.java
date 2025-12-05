package com.smarttruck.presentation.dto;

import jakarta.validation.constraints.NotBlank;

import java.time.Instant;

/**
 * DTO de resposta após criação de um ticket. Contém campos essenciais que o cliente da API pode
 * usar para rastrear o ticket.
 */
public record CreateTicketResponse(String id, @NotBlank String customerId,
                                   @NotBlank String description, String status, Instant createdAt,
                                   Instant updatedAt, Instant deletedAt) {
}
