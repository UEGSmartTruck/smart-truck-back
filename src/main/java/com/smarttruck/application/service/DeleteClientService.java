package com.smarttruck.application.service;

import com.smarttruck.application.usecase.DeleteClientUseCase;
import com.smarttruck.domain.repository.ClientRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class DeleteClientService implements DeleteClientUseCase {

    private final ClientRepository repository;

    public DeleteClientService(ClientRepository repository) {
        this.repository = repository;
    }

    @Override
    public void execute(UUID id) {
        repository.findById(id.toString())
            .orElseThrow(() -> new IllegalArgumentException("Client not found"));

        repository.deleteById(id.toString());
    }
}
