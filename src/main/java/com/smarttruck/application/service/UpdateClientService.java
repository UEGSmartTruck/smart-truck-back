package com.smarttruck.application.service;

import com.smarttruck.application.usecase.UpdateClientUseCase;
import com.smarttruck.domain.model.Client;
import com.smarttruck.domain.repository.ClientRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UpdateClientService implements UpdateClientUseCase {

    private final ClientRepository clientRepository;

    public UpdateClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Override
    public Client execute(UUID id, String name, String email, String phone) {

        Client client = clientRepository.findById(id.toString())
            .orElseThrow(() -> new IllegalArgumentException("Client not found"));

        if (!client.getEmail().equals(email) && clientRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Email already in use");
        }

        client.setName(name);
        client.setEmail(email);
        client.setPhone(phone);

        return clientRepository.save(client);
    }
}
