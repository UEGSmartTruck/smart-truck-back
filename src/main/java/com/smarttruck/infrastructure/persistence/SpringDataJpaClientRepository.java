package com.smarttruck.infrastructure.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface SpringDataJpaClientRepository extends JpaRepository<JpaClient, String> {

    boolean existsByEmail(String email);
    Optional<JpaClient> findByEmail(String email);

}
