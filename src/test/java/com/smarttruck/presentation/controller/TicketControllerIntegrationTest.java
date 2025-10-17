package com.smarttruck.presentation.controller;

import com.smarttruck.application.usecase.CreateTicketUseCase;
import com.smarttruck.domain.model.Ticket;
import com.smarttruck.domain.model.TicketStatus;
import com.smarttruck.shared.security.JwtTokenProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;

import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc(addFilters = false) // desativa filtros de segurança para testes
@WebMvcTest(TicketController.class)
@ContextConfiguration(classes = {TicketController.class}) // carrega apenas o controller
class TicketControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CreateTicketUseCase createTicketUseCase;

    @MockitoBean
    private JwtTokenProvider jwtTokenProvider;

    private Ticket ticket;

    @BeforeEach
    void setUp() {
        ticket = new Ticket("abc-123", "cust-001", "Motor overheating", TicketStatus.OPEN,
            Instant.parse("2024-10-10T10:10:10Z"), null, null);
    }

    @Test
    void shouldReturn200AndResponseBody_whenRequestIsValid() throws Exception {
        String requestJson = """
                {
                  "customerId": "cust-001",
                  "description": "Motor overheating",
                  "aiSolved": false
                }
            """;

        Mockito.when(createTicketUseCase.execute(anyString(), anyString(), anyBoolean()))
            .thenReturn(ticket);

        mockMvc.perform(
                post("/tickets").contentType(MediaType.APPLICATION_JSON).content(requestJson))
            .andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value("abc-123"))
            .andExpect(jsonPath("$.customerId").value("cust-001"))
            .andExpect(jsonPath("$.description").value("Motor overheating"))
            .andExpect(jsonPath("$.status").value("OPEN"))
            .andExpect(jsonPath("$.createdAt").value("2024-10-10T10:10:10Z"));
    }

    @Test
    void shouldSetAiSolvedToFalse_whenFieldIsMissing() throws Exception {
        String requestJson = """
                {
                  "customerId": "cust-002",
                  "description": "Brake problem"
                }
            """;

        Mockito.when(createTicketUseCase.execute("cust-002", "Brake problem", false))
            .thenReturn(ticket);

        mockMvc.perform(
                post("/tickets").contentType(MediaType.APPLICATION_JSON).content(requestJson))
            .andExpect(status().isOk()).andExpect(jsonPath("$.id").value("abc-123"))
            .andExpect(jsonPath("$.customerId").value("cust-001"));
    }

    @Test
    void shouldReturn400_whenRequestIsInvalid() throws Exception {
        String invalidJson = "{}";

        mockMvc.perform(
                post("/tickets").contentType(MediaType.APPLICATION_JSON).content(invalidJson))
            .andExpect(status().isBadRequest());
    }
}
