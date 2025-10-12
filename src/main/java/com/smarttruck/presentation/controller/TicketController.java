package com.smarttruck.presentation.controller;

import com.smarttruck.application.service.CreateTicketUseCase;
import com.smarttruck.presentation.dto.CreateTicketRequest;
import com.smarttruck.presentation.dto.CreateTicketResponse;
import com.smarttruck.presentation.mapper.TicketMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller REST que expõe endpoints para gerenciamento de tickets.
 *
 * Endpoints:
 * - POST /api/tickets : cria um novo ticket a partir do payload
 * {@link CreateTicketRequest}
 */
@RestController
@RequestMapping("/api/tickets")
public class TicketController {

    private final CreateTicketUseCase createTicketUseCase;

    public TicketController(CreateTicketUseCase createTicketUseCase) {
        this.createTicketUseCase = createTicketUseCase;
    }

    /**
     * Cria um ticket a partir do request JSON.
     *
     * @param request payload contendo customerId, description e opcionalmente
     *                aiSolved
     * @return {@link CreateTicketResponse} com os dados do ticket criado
     */
    @PostMapping
    public ResponseEntity<CreateTicketResponse> create(@RequestBody CreateTicketRequest request) {
        boolean aiSolved = request.getAiSolved() != null ? request.getAiSolved() : false;

        var ticket = createTicketUseCase.create(request.getCustomerId(), request.getDescription(), aiSolved);
        var response = TicketMapper.toResponse(ticket);
        return ResponseEntity.ok(response);
    }
}
