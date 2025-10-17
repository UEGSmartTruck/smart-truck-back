package com.smarttruck.presentation.mapper;

import com.smarttruck.domain.model.User;
import com.smarttruck.presentation.dto.CreateUserResponse;
import com.smarttruck.presentation.dto.UserResponse; // <-- 1. Importe o novo DTO
import org.springframework.stereotype.Component;

import java.util.List; // <-- 2. Importe List
import java.util.stream.Collectors; // <-- 3. Importe Collectors

@Component
public class UserMapper {

    private UserMapper() {
    }

    // SEU MÉTODO ORIGINAL (intacto)
    // Usado pelo endpoint de criação de usuário (POST /users)
    public static CreateUserResponse toResponse(User user) {
        CreateUserResponse r = new CreateUserResponse();
        r.setId(user.getId());
        r.setName(user.getName());
        r.setEmail(user.getEmail());
        r.setPhone(user.getPhone());
        r.setCreatedAt(user.getCreatedAt());
        r.setUpdatedAt(user.getUpdatedAt());
        r.setDeletedAt(user.getDeletedAt());
        r.setLoginAt(user.getLoginAt());
        return r;
    }

    // --- NOVOS MÉTODOS ADICIONADOS ---

    /**
     * Converte uma entidade User para um UserResponseDTO.
     * Este DTO é usado especificamente para a listagem de usuários.
     */
    private static UserResponse toUserResponseDTO(User user) {
        return new UserResponse(
            user.getId(),
            user.getName(),
            user.getEmail(),
            user.getPhone(),
            user.getCreatedAt()
        );
    }

    /**
     * Converte uma lista de entidades User para uma lista de UserResponseDTOs.
     * Usado pelo endpoint de listagem de todos os usuários (GET /users).
     */
    public static List<UserResponse> toResponseList(List<User> users) {
        return users.stream()
            .map(UserMapper::toUserResponseDTO)
            .collect(Collectors.toList());
    }
}
