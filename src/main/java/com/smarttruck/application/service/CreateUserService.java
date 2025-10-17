package com.smarttruck.application.service;


import com.smarttruck.application.usecase.CreateUserUseCase;
import com.smarttruck.domain.model.User;
import com.smarttruck.domain.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class CreateUserService implements CreateUserUseCase {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public CreateUserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User execute(String name, String email, String password, String phone) {
        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Email already in use");
        }

        String passwordHash = passwordEncoder.encode(password);

        User user = new User(name, phone, email, passwordHash);
        return userRepository.save(user);
    }
}

