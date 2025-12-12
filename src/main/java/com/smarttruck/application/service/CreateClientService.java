package com.smarttruck.application.service;

import com.smarttruck.application.usecase.CreateClientUseCase;
import com.smarttruck.domain.model.Client;
import com.smarttruck.domain.repository.ClientRepository;
import org.springframework.stereotype.Service;

@Service
public class CreateClientService implements CreateClientUseCase {


    private final ClientRepository clientRepository;

    public CreateClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Override
    public Client execute(String name, String email, String phone) {
        if (clientRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Email already in use");
        }

        Client client = new Client(name, email, phone);
        return clientRepository.save(client);
    }
}
