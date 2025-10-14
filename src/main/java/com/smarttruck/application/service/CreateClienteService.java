package com.smarttruck.application.service;

import com.smarttruck.domain.model.Cliente;
import com.smarttruck.domain.repository.ClienteRepository;
import org.springframework.stereotype.Service;

@Service
public class CreateClienteService {

    private final ClienteRepository clienteRepository;

    public CreateClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    public Cliente execute(String nome, String cpf, String telefone, String email) {
        Cliente cliente = new Cliente(null, nome, cpf, telefone, email);
        return clienteRepository.save(cliente);
    }
}

