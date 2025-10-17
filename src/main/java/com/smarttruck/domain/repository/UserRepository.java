package com.smarttruck.domain.repository;

import com.smarttruck.domain.model.User;

import java.util.Optional;

public interface UserRepository {
    User save(User user);

    boolean existsByEmail(String email);
    Optional<User> findByEmail(String email);
}
