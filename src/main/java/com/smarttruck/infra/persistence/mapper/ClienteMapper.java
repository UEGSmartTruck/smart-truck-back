package com.smarttruck.infra.persistence.mapper;

import com.smarttruck.domain.model.Cliente;
import com.smarttruck.infra.entity.ClienteEntity;

public class ClienteMapper {

    public static Cliente toDomain(ClienteEntity entity) {
        return new Cliente(
            entity.getId(),
            entity.getNome(),
            entity.getCpf(),
            entity.getTelefone(),
            entity.getEmail()
        );
    }

    public static ClienteEntity toEntity(Cliente cliente) {
        ClienteEntity entity = new ClienteEntity();
        entity.setId(cliente.getId());
        entity.setNome(cliente.getNome());
        entity.setCpf(cliente.getCpf());
        entity.setTelefone(cliente.getTelefone());
        entity.setEmail(cliente.getEmail());
        return entity;
    }
}

