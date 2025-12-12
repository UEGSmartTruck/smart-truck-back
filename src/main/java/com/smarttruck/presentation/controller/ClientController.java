package com.smarttruck.presentation.controller;

import com.smarttruck.application.usecase.CreateClientUseCase;
import com.smarttruck.application.usecase.DeleteClientUseCase;
import com.smarttruck.application.usecase.ListAllClientUseCase;
import com.smarttruck.application.usecase.UpdateClientUseCase;
import com.smarttruck.presentation.dto.CreateClientRequest;
import com.smarttruck.presentation.dto.CreateClientResponse;
import com.smarttruck.presentation.dto.ListAllClientResponse;
import com.smarttruck.presentation.dto.UpdateClientRequest;
import com.smarttruck.presentation.mapper.ClientMapper;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/clients")
@Validated
public class ClientController {

    private final CreateClientUseCase createClientUseCase;
    private final ListAllClientUseCase listAllClientUseCase;
    private final UpdateClientUseCase updateClientUseCase;
    private final DeleteClientUseCase deleteClientUseCase;

    public ClientController(
        CreateClientUseCase createClientUseCase,
        ListAllClientUseCase listAllClientUseCase,
        UpdateClientUseCase updateClientUseCase,
        DeleteClientUseCase deleteClientUseCase
    ) {
        this.createClientUseCase = createClientUseCase;
        this.listAllClientUseCase = listAllClientUseCase;
        this.updateClientUseCase = updateClientUseCase;
        this.deleteClientUseCase = deleteClientUseCase;
    }

    @PostMapping
    public ResponseEntity<CreateClientResponse> create(
        @Valid @RequestBody CreateClientRequest request) {

        var client = createClientUseCase.execute(
            request.name(),
            request.email(),
            request.phone()
        );

        if (client == null) return ResponseEntity.badRequest().build();

        return ResponseEntity.ok(ClientMapper.toResponse(client));
    }

    // LIST ALL
    @GetMapping
    public ResponseEntity<ListAllClientResponse> findAll(
        @RequestParam(defaultValue = "0") @Min(0) int page,
        @RequestParam(defaultValue = "20") @Min(1) @Max(100) int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        var clientPage = listAllClientUseCase.execute(pageable);
        return ResponseEntity.ok(ClientMapper.toListAllResponse(clientPage));
    }

    // LIST BY ID
    @GetMapping("/{id}")
    public ResponseEntity<CreateClientResponse> findById(@PathVariable String id) {
        return listAllClientUseCase.findById(id)
            .map(client -> ResponseEntity.ok(ClientMapper.toResponse(client)))
            .orElse(ResponseEntity.notFound().build());
    }

    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<CreateClientResponse> update(
        @PathVariable String id,
        @Valid @RequestBody UpdateClientRequest request) {

        var updatedClient = updateClientUseCase.execute(
            UUID.fromString(id),
            request.name(),
            request.email(),
            request.phone()
        );

        if (updatedClient == null) return ResponseEntity.notFound().build();

        return ResponseEntity.ok(ClientMapper.toResponse(updatedClient));
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        deleteClientUseCase.execute(UUID.fromString(id));
        return ResponseEntity.noContent().build();
    }
}
