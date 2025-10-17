package com.smarttruck.domain.repository;

import com.smarttruck.domain.model.User;

import java.util.List; // Importar List
import java.util.Optional;

public interface UserRepository {
    User save(User user);
    boolean existsByEmail(String email);
    Optional<User> findByEmail(String email);

    // ADICIONE ESTA LINHA
    List<User> findAll();
}
