package com.smarttruck.presentation.mapper;

import com.smarttruck.domain.model.Client;
import com.smarttruck.presentation.dto.ClientData;
import com.smarttruck.presentation.dto.CreateClientResponse;
import com.smarttruck.presentation.dto.ListAllClientResponse;
import com.smarttruck.presentation.dto.PaginationMetadata;
import org.springframework.data.domain.Page;

import java.util.List;

public class ClientMapper {

    private ClientMapper() {
    }

    public static CreateClientResponse toResponse(final Client client) {
        final CreateClientResponse r =
            new CreateClientResponse(client.getId(), client.getName(), client.getEmail(), client.getPhone());
        return r;
    }

    public static ClientData toClientData(Client client) {
        return new ClientData(
            client.getId(),
            client.getName(),
            client.getEmail(),
            client.getPhone()
        );
    }

    public static ListAllClientResponse toListAllResponse(Page<Client> page) {
        List<ClientData> clients = page.getContent().stream()
            .map(ClientMapper::toClientData)
            .toList();

        PaginationMetadata metadata = new PaginationMetadata(
            page.getTotalElements(),
            page.getTotalPages(),
            page.getNumber(),
            page.getSize()
        );

        return new ListAllClientResponse(clients, metadata);
    }
}
