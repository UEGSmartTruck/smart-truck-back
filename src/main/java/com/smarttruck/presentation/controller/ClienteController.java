package com.smarttruck.presentation.controller;

import com.smarttruck.application.service.CreateClienteService;
import com.smarttruck.domain.model.Cliente;
import com.smarttruck.domain.repository.ClienteRepository;
import com.smarttruck.presentation.dto.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/clientes")
public class ClienteController {

    private final ClienteRepository clienteRepository;
    private final CreateClienteService createClienteService;

    public ClienteController(ClienteRepository clienteRepository, CreateClienteService createClienteService) {
        this.clienteRepository = clienteRepository;
        this.createClienteService = createClienteService;
    }

    @PostMapping
    public ClienteResponse create(@RequestBody CreateClienteRequest request) {
        Cliente cliente = createClienteService.execute(
            request.getNome(),
            request.getCpf(),
            request.getTelefone(),
            request.getEmail()
        );
        return new ClienteResponse(
            cliente.getId(),
            cliente.getNome(),
            cliente.getCpf(),
            cliente.getTelefone(),
            cliente.getEmail()
        );
    }

    @GetMapping
    public List<Cliente> findAll() {
        return clienteRepository.findAll();
    }

    @GetMapping("/{id}")
    public Cliente findById(@PathVariable Long id) {
        return clienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));
    }

    @PutMapping("/{id}")
    public Cliente update(@PathVariable Long id, @RequestBody UpdateClienteRequest request) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));
        cliente.update(request.getNome(), request.getTelefone(), request.getEmail());
        return clienteRepository.save(cliente);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        clienteRepository.deleteById(id);
    }
}

