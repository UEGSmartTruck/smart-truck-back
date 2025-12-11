package com.smarttruck.domain.repository;

import com.smarttruck.domain.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    User save(User user);

    boolean existsByEmail(String email);
    Optional<User> findByEmail(String email);

    // --- NOVO MeTODO NECESSÁRIO PARA O PUT ---
    Optional<User> findById(String id);
    // -----------------------------------------

    List<User> findAll();

    /**
     * Retorna página de usuários ativos (deletedAt IS NULL).
     *
     * @param pageable parâmetros de paginação (page, size, sort)
     * @return página contendo usuários ativos
     */
    Page<User> findAllActive(Pageable pageable);
}
