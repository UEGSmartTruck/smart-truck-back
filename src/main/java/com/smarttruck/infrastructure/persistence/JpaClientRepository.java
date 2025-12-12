package com.smarttruck.infrastructure.persistence;

import com.smarttruck.domain.model.Client;
import com.smarttruck.domain.repository.ClientRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public class JpaClientRepository implements ClientRepository {

    private final ClientJpaRepository springRepo;
    private final JpaClientMapper mapper;

    public JpaClientRepository(ClientJpaRepository springRepo, JpaClientMapper mapper) {
        this.springRepo = springRepo;
        this.mapper = mapper;
    }

    @Override
    @Transactional
    public Client save(Client client) {
        JpaClient entity = mapper.toEntity(client);
        JpaClient saved = springRepo.save(entity);
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<Client> findById(String id) {
        return springRepo.findById(id).map(mapper::toDomain);
    }

    @Override
    public boolean existsByEmail(String email) {
        return springRepo.existsByEmail(email);
    }

    @Override
    public Optional<Client> findByEmail(String email) {
        return springRepo.findByEmail(email).map(mapper::toDomain);
    }

    @Override
    public List<Client> findAll() {
        return springRepo.findAll().stream().map(mapper::toDomain).toList();
    }

    @Override
    @Transactional
    public void deleteById(String id) {
        springRepo.deleteById(id);
    }

    @Override
    public Page<Client> findAllPageable(Pageable pageable) {
        return springRepo.findAll(pageable).map(mapper::toDomain);
    }
}
