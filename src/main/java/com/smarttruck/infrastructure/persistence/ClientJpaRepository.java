package com.smarttruck.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClientJpaRepository extends JpaRepository<JpaClient, String> {

    boolean existsByEmail(String email);
    Optional<JpaClient> findByEmail(String email);


}
