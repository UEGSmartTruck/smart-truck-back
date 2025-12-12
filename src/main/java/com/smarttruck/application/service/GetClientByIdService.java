package com.smarttruck.application.service;

import com.smarttruck.application.usecase.GetClientByIdUseCase;
import com.smarttruck.domain.model.Client;
import com.smarttruck.domain.repository.ClientRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class GetClientByIdService implements GetClientByIdUseCase {

    private final ClientRepository clientRepository;

    public GetClientByIdService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public Client execute(String id) {
        Optional<Client> maybe = clientRepository.findById(id);
        return maybe.orElse(null);
    }
}
