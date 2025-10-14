package com.smarttruck.infra.repository;

import com.smarttruck.domain.model.Cliente;
import com.smarttruck.domain.repository.ClienteRepository;
import com.smarttruck.infra.persistence.mapper.ClienteMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class JpaClienteRepository implements ClienteRepository {

    private final SpringDataClienteRepository repository;

    public JpaClienteRepository(SpringDataClienteRepository repository) {
        this.repository = repository;
    }

    @Override
    public Cliente save(Cliente cliente) {
        return ClienteMapper.toDomain(repository.save(ClienteMapper.toEntity(cliente)));
    }

    @Override
    public List<Cliente> findAll() {
        return repository.findAll().stream().map(ClienteMapper::toDomain).toList();
    }

    @Override
    public Optional<Cliente> findById(Long id) {
        return repository.findById(id).map(ClienteMapper::toDomain);
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}

