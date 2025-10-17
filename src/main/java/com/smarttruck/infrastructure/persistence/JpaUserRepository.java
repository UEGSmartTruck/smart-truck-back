package com.smarttruck.infrastructure.persistence;


import com.smarttruck.domain.model.User;
import com.smarttruck.domain.repository.UserRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Repository
public class JpaUserRepository implements UserRepository {


    private final SpringDataJpaUserRepository springRepo;
    private final JpaUserMapper mapper;


    public JpaUserRepository(SpringDataJpaUserRepository springRepo, JpaUserMapper mapper) {
        this.springRepo = springRepo;
        this.mapper = mapper;
    }


    @Override
    @Transactional
    public User save(User user) {
        JpaUser entity = mapper.toEntity(user);
        JpaUser saved = springRepo.save(entity);
        return mapper.toDomain(saved);
    }


    @Override
    public boolean existsByEmail(String email) {
        return springRepo.existsByEmail(email);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return springRepo.findByEmail(email)
            .map(mapper::toDomain);
    }
}
