// src/main/java/com/smarttruck/application/usecase/AuthenticateUserUseCase.java
package com.smarttruck.application.usecase;

import com.smarttruck.domain.model.User;
import com.smarttruck.domain.repository.UserRepository;
import com.smarttruck.presentation.dto.LoginRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticateUserUseCase {
    private final UserRepository userRepository;

    public Optional<User> execute(LoginRequest loginRequest) {
        Optional<User> userOptional = userRepository.findByEmail(loginRequest.getEmail());
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (user.getPassword().equals(loginRequest.getPassword())) {
                return Optional.of(user);
            }
        }
        return Optional.empty();
    }
}
