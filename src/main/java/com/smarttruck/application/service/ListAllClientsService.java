package com.smarttruck.application.service;

import com.smarttruck.application.usecase.ListAllClientsUseCase;
import com.smarttruck.domain.model.Client;
import com.smarttruck.domain.repository.ClientRepository;
import java.util.List;

public class ListAllClientsService implements ListAllClientsUseCase {

    private final ClientRepository clientRepository;

    public ListAllClientsService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Override
    public List<Client> execute() {
        return clientRepository.findAll();
    }
}
