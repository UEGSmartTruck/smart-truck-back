package com.smarttruck.infrastructure.persistence;

import com.smarttruck.domain.model.User;
import com.smarttruck.domain.repository.UserRepository;
import com.smarttruck.infrastructure.persistence.JpaUserMapper; // Corrigido o import
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class JpaUserRepository implements UserRepository {

    private final SpringDataJpaUserRepository springDataJpaUserRepository;
    private final JpaUserMapper jpaUserMapper;

    public JpaUserRepository(SpringDataJpaUserRepository springDataJpaUserRepository, JpaUserMapper jpaUserMapper) {
        this.springDataJpaUserRepository = springDataJpaUserRepository;
        this.jpaUserMapper = jpaUserMapper;
    }

    @Override
    public User save(User user) {
        // ANTES (ERRADO):
        // var jpaUser = jpaUserMapper.toJpa(user);

        // DEPOIS (CORRETO):
        var jpaUser = jpaUserMapper.toEntity(user); // Use o nome correto do método: toEntity

        var savedJpaUser = springDataJpaUserRepository.save(jpaUser);
        return jpaUserMapper.toDomain(savedJpaUser);
    }

    @Override
    public boolean existsByEmail(String email) {
        return springDataJpaUserRepository.existsByEmail(email);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return springDataJpaUserRepository.findByEmail(email)
            .map(jpaUserMapper::toDomain);
    }

    @Override
    public List<User> findAll() {
        return springDataJpaUserRepository.findAll()
            .stream()
            .map(jpaUserMapper::toDomain)
            .collect(Collectors.toList());
    }
}
