package com.smarttruck.presentation.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smarttruck.application.usecase.CreateTicketUseCase;
import com.smarttruck.domain.model.Ticket;
import com.smarttruck.domain.model.TicketStatus;
import com.smarttruck.presentation.dto.CreateTicketRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;

import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TicketController.class)
class TicketControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CreateTicketUseCase createTicketUseCase;

    @Autowired
    private ObjectMapper objectMapper;

    private Ticket ticket;

    @BeforeEach
    void setUp() {
        ticket = new Ticket(
                "abc-123",
                "cust-001",
                "Motor overheating",
                TicketStatus.OPEN,
                Instant.parse("2024-10-10T10:10:10Z"),
                null,
                null);
    }

    @Test
    void shouldReturn200AndResponseBody_whenRequestIsValid() throws Exception {
        // Arrange
        CreateTicketRequest request = new CreateTicketRequest();
        request.setCustomerId("cust-001");
        request.setDescription("Motor overheating");
        request.setAiSolved(false);

        Mockito.when(createTicketUseCase.create(anyString(), anyString(), anyBoolean()))
                .thenReturn(ticket);

        // Act & Assert
        mockMvc.perform(post("/api/tickets")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                // Testa campos do JSON de resposta
                .andExpect(jsonPath("$.id").value("abc-123"))
                .andExpect(jsonPath("$.customerId").value("cust-001"))
                .andExpect(jsonPath("$.description").value("Motor overheating"))
                .andExpect(jsonPath("$.status").value("OPEN"))
                .andExpect(jsonPath("$.createdAt").value("2024-10-10T10:10:10Z"));
    }

    @Test
    void shouldSetAiSolvedToFalse_whenFieldIsMissing() throws Exception {
        // Arrange: campo aiSolved ausente → deve ser tratado como false
        CreateTicketRequest request = new CreateTicketRequest();
        request.setCustomerId("cust-002");
        request.setDescription("Brake problem");

        Mockito.when(createTicketUseCase.create("cust-002", "Brake problem", false))
                .thenReturn(ticket);

        // Act & Assert
        mockMvc.perform(post("/api/tickets")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("abc-123"))
                .andExpect(jsonPath("$.customerId").value("cust-001")); // ticket mockado sempre retorna esse valor
    }

    @Test
    void shouldReturn400_whenRequestIsInvalid() throws Exception {
        // Arrange: request sem campos obrigatórios
        CreateTicketRequest invalidRequest = new CreateTicketRequest();

        // Act & Assert
        mockMvc.perform(post("/api/tickets")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }
}
