// src/main/java/com/smarttruck/infra/persistence/repository/JpaUserRepository.java
package com.smarttruck.infrastructure.persistence.repository;

import com.smarttruck.domain.model.User;
import com.smarttruck.domain.repository.UserRepository;
import com.smarttruck.infrastructure.entity.UserEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

// Esta classe "adapta" o repositório do Spring Data para o contrato do nosso domínio
@Component // Marcado como um Bean do Spring
public class JpaUserRepository implements UserRepository {

    private final SpringDataJpaUserRepo repo; // A interface real do Spring Data JPA

    public JpaUserRepository(SpringDataJpaUserRepo repo) {
        this.repo = repo;
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return repo.findByEmail(email).map(this::toDomainModel);
    }

    @Override
    public Optional<User> findById(Long id) {
        return repo.findById(id).map(this::toDomainModel);
    }

    @Override
    public List<User> findAll() {
        return repo.findAll().stream().map(this::toDomainModel).collect(Collectors.toList());
    }

    // Metodo privado para converter de Entity para Model de Domínio
    private User toDomainModel(UserEntity entity) {
        return new User(entity.getId(), entity.getEmail(), entity.getPassword());
    }
}

