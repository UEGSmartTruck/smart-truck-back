package com.smarttruck.domain.repository;

import com.smarttruck.domain.model.User;

public interface UserRepository {
    User save(User user);

    boolean existsByEmail(String email);
}
