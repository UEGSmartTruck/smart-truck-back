package com.smarttruck.presentation.dto;

/**
 * Payload de requisição para criação de ticket via API.
 * <p>
 * Exemplo JSON:
 * {
 * "customerId": "cliente-123",
 * "description": "pane no caminhão",
 * "aiSolved": true
 * }
 */
public record CreateTicketRequest(String customerId, String description, Boolean aiSolved) {
}
