package com.smarttruck.presentation.controller;

import com.smarttruck.application.usecase.CreateTicketUseCase;
import com.smarttruck.domain.model.Ticket;
import com.smarttruck.presentation.dto.CreateTicketRequest;
import com.smarttruck.presentation.dto.CreateTicketResponse;
import com.smarttruck.presentation.mapper.TicketMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class TicketControllerTest {

    private CreateTicketUseCase createTicketUseCase;
    private TicketController controller;

    @BeforeEach
    void setUp() {
        createTicketUseCase = mock(CreateTicketUseCase.class);
        controller = new TicketController(createTicketUseCase);
    }

    @Test
    void shouldCreateTicketSuccessfully_whenAiSolvedIsTrue() {
        // Arrange
        CreateTicketRequest request = new CreateTicketRequest("123", "Motor issue", true);

        Ticket ticket = new Ticket("1", "123", "Motor issue", null, Instant.now(), null, null);
        CreateTicketResponse expectedResponse =
            new CreateTicketResponse("1", "123", "Motor issue", "CREATED", Instant.now());

        when(createTicketUseCase.execute("123", "Motor issue", true)).thenReturn(ticket);

        try (MockedStatic<TicketMapper> mapperMock = Mockito.mockStatic(TicketMapper.class)) {
            mapperMock.when(() -> TicketMapper.toResponse(ticket)).thenReturn(expectedResponse);

            // Act
            ResponseEntity<CreateTicketResponse> responseEntity = controller.create(request);

            // Assert
            assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
            assertEquals(expectedResponse, responseEntity.getBody());
            verify(createTicketUseCase).execute("123", "Motor issue", true);
        }
    }

    @Test
    void shouldCreateTicketWithAiSolvedFalse_whenAiSolvedIsNull() {
        // Arrange — aiSolved = null (Jackson pode desserializar assim se campo vier ausente)
        CreateTicketRequest request = new CreateTicketRequest("999", "Brake check", null);

        Ticket ticket = new Ticket("2", "999", "Brake check", null, Instant.now(), null, null);
        CreateTicketResponse expectedResponse =
            new CreateTicketResponse("2", "999", "Brake check", "CREATED", Instant.now());

        when(createTicketUseCase.execute("999", "Brake check", false)).thenReturn(ticket);

        try (MockedStatic<TicketMapper> mapperMock = Mockito.mockStatic(TicketMapper.class)) {
            mapperMock.when(() -> TicketMapper.toResponse(ticket)).thenReturn(expectedResponse);

            // Act
            ResponseEntity<CreateTicketResponse> responseEntity = controller.create(request);

            // Assert
            assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
            assertEquals(expectedResponse, responseEntity.getBody());
            verify(createTicketUseCase).execute("999", "Brake check", false);
        }
    }
}
