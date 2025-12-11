package com.smarttruck.application.service;

import com.smarttruck.application.usecase.UpdateUserUseCase;
import com.smarttruck.domain.model.User;
import com.smarttruck.domain.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UpdateUserService implements UpdateUserUseCase {

    private final UserRepository userRepository;

    public UpdateUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User execute(UUID id, String name, String email, String phone) {
        // 1. Busca o usuário ou lança erro se não existir
        User user = userRepository.findById(id.toString())
            .orElseThrow(() -> new IllegalArgumentException("User not found"));

        // 2. Validação de Email Único:
        // Só reclama se o email mudou E se o novo email já existe no banco
        if (!user.getEmail().equals(email) && userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Email already in use");
        }

        // 3. Atualiza os dados (Assumindo que sua entidade User tem esses setters)
        user.setName(name);
        user.setEmail(email);
        user.setPhone(phone);

        // 4. Salva e retorna o usuário atualizado
        return userRepository.save(user);
    }
}
