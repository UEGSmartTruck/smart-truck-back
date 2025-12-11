package com.smarttruck.infrastructure.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserJpaRepository extends JpaRepository<JpaUser, String> {

    boolean existsByEmail(String email);

    Page<JpaUser> findAllByDeletedAtIsNull(Pageable pageable);
}
