// src/main/java/com/smarttruck/domain/repository/UserRepository.java
package com.smarttruck.domain.repository;

import com.smarttruck.domain.model.User;
import java.util.List;
import java.util.Optional;

// Este é o contrato. O domínio só sabe que existe um lugar para buscar usuários.
public interface UserRepository {
    Optional<User> findByEmail(String email);
    Optional<User> findById(Long id);
    List<User> findAll();
    // save, delete, etc., poderiam ser adicionados aqui
}
