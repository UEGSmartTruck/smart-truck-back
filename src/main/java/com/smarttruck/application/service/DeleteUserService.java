package com.smarttruck.application.service;

import com.smarttruck.application.usecase.DeleteUserUseCase;
import com.smarttruck.domain.model.User;
import com.smarttruck.domain.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
public class DeleteUserService implements DeleteUserUseCase {

    private final UserRepository repository;

    public DeleteUserService(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public void execute(UUID id) {
        User user = repository.findById(id.toString())
            .orElseThrow(() -> new IllegalArgumentException("User not found"));

        user.setDeletedAt(Instant.now());

        repository.save(user);
    }
}
