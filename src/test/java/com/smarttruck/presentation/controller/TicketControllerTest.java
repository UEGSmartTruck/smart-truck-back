package com.smarttruck.presentation.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.time.Instant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.smarttruck.application.usecase.CreateTicketUseCase;
import com.smarttruck.domain.model.Ticket;
import com.smarttruck.domain.model.TicketStatus;
import com.smarttruck.presentation.dto.CreateTicketRequest;
import com.smarttruck.presentation.dto.CreateTicketResponse;
import com.smarttruck.presentation.mapper.TicketMapper;

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
        final CreateTicketRequest request = new CreateTicketRequest("123", "Motor issue", true);

        final Instant now = Instant.now();
        final Ticket ticket =
                new Ticket("1", "123", "Motor issue", TicketStatus.OPEN, now, now, null);
        final CreateTicketResponse expectedResponse =
                new CreateTicketResponse("1", "123", "Motor issue", "OPEN", now, now, null);

        when(createTicketUseCase.execute("123", "Motor issue", true)).thenReturn(ticket);

        try (MockedStatic<TicketMapper> mapperMock = Mockito.mockStatic(TicketMapper.class)) {
            mapperMock.when(() -> TicketMapper.toResponse(ticket)).thenReturn(expectedResponse);

            // Act
            final ResponseEntity<CreateTicketResponse> responseEntity = controller.create(request);

            // Assert
            assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
            assertEquals(expectedResponse, responseEntity.getBody());
            verify(createTicketUseCase).execute("123", "Motor issue", true);
        }
    }

    @Test
    void shouldCreateTicketWithAiSolvedFalse_whenAiSolvedIsNull() {
        // Arrange — aiSolved = null (Jackson pode desserializar assim se campo vier ausente)
        final CreateTicketRequest request = new CreateTicketRequest("999", "Brake check", null);

        final Instant now = Instant.now();
        final Ticket ticket =
                new Ticket("2", "999", "Brake check", TicketStatus.OPEN, now, now, null);
        final CreateTicketResponse expectedResponse =
                new CreateTicketResponse("2", "999", "Brake check", "OPEN", now, now, null);

        when(createTicketUseCase.execute("999", "Brake check", false)).thenReturn(ticket);

        try (MockedStatic<TicketMapper> mapperMock = Mockito.mockStatic(TicketMapper.class)) {
            mapperMock.when(() -> TicketMapper.toResponse(ticket)).thenReturn(expectedResponse);

            // Act
            final ResponseEntity<CreateTicketResponse> responseEntity = controller.create(request);

            // Assert
            assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
            assertEquals(expectedResponse, responseEntity.getBody());
            verify(createTicketUseCase).execute("999", "Brake check", false);
        }
    }
}
