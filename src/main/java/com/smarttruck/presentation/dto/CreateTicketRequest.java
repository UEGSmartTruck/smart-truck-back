package com.smarttruck.presentation.dto;

/**
 * Payload de requisição para criação de ticket via API.
 *
 * Exemplo JSON:
 * {
 * "customerId": "cliente-123",
 * "description": "pane no caminhão",
 * "aiSolved": true
 * }
 */
public class CreateTicketRequest {
    private String customerId;
    private String description;
    private Boolean aiSolved;

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

    public Boolean getAiSolved() {
        return aiSolved;
    }

    public void setAiSolved(Boolean aiSolved) {
        this.aiSolved = aiSolved;
    }
}
