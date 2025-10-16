package com.smarttruck.infrastructure.persistence.repository;

import com.smarttruck.infrastructure.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

// Interface auxiliar que realmente estende JpaRepository
public interface SpringDataJpaUserRepo extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByEmail(String email);
}
