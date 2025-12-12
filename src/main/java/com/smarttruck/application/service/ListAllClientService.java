package com.smarttruck.application.service;

import com.smarttruck.application.usecase.ListAllClientUseCase;
import com.smarttruck.domain.model.Client;
import com.smarttruck.domain.repository.ClientRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ListAllClientService implements ListAllClientUseCase {

    private final ClientRepository clientRepository;

    public ListAllClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Override
    public Page<Client> execute(Pageable pageable) {
        return clientRepository.findAllPageable(pageable);
    }

    @Override
    public Optional<Client> findById(String id) {
        return clientRepository.findById(id);
    }
}
