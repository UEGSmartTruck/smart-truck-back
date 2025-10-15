package com.smarttruck.infrastructure.persistence;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface SpringDataJpaUserRepository extends JpaRepository<JpaUser, String> {
    boolean existsByEmail(String email);
}
