package com.smarttruck.application.service;

import com.smarttruck.application.usecase.AuthenticateUserUseCase;
import com.smarttruck.domain.model.User;
import com.smarttruck.domain.repository.UserRepository;
import com.smarttruck.shared.security.JwtTokenProvider;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticateUserService implements AuthenticateUserUseCase {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthenticateUserService(UserRepository userRepository, PasswordEncoder passwordEncoder,
                                   JwtTokenProvider jwtTokenProvider) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public String execute(String email, String password) {
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        if (!passwordEncoder.matches(password, user.getPasswordHash())) {
            throw new RuntimeException("Senha inválida");
        }

        return jwtTokenProvider.generateToken(user.getId(), user.getEmail());
    }
}
