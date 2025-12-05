package com.smarttruck.infrastructure.persistence;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface SpringDataJpaUserRepository extends JpaRepository<JpaUser, String> {
    boolean existsByEmail(String email);
    Optional<JpaUser> findByEmail(String email);

    /**
     * Busca usuários ativos (deleted_at IS NULL) com paginação.
     *
     * @param pageable parâmetros de paginação
     * @return página de usuários ativos
     */
    @Query("SELECT u FROM JpaUser u WHERE u.deletedAt IS NULL")
    Page<JpaUser> findAllActive(Pageable pageable);
}
