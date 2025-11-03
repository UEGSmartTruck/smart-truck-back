package com.smarttruck.presentation.mapper;

import com.smarttruck.domain.model.User;
import com.smarttruck.presentation.dto.CreateUserRequest;
import com.smarttruck.presentation.dto.CreateUserResponse;
import com.smarttruck.presentation.dto.UserResponse; // 1. Importar o novo DTO
import java.util.List; // 2. Importar List
import java.util.stream.Collectors; // 3. Importar Collectors

// 4. Vamos assumir um Mapper estático, baseado no seu código do controller
public class UserMapper {

    // Seu metodo estático existente
    public static CreateUserResponse toResponse(User user) {
        return new CreateUserResponse(
            user.getId(),
            user.getName(),
            user.getEmail(),
            user.getPhone(),
            user.getCreatedAt(),
            user.getUpdatedAt(),
            user.getDeletedAt(),
            user.getLoginAt()
        );
    }

    // 5. Novo metodo para converter um único User para UserResponse
    public static UserResponse toUserResponse(User user) {
        return new UserResponse(
            user.getId(),
            user.getName(),
            user.getEmail(),
            user.getPhone(),
            user.getCreatedAt(),
            user.getUpdatedAt()
        );
    }

    // 6. Novo metodo para converter uma Lista de Users
    public static List<UserResponse> toUserResponseList(List<User> users) {
        return users.stream()
            .map(UserMapper::toUserResponse)
            .collect(Collectors.toList());
    }

    // Nota: Se você usa MapStruct (@Mapper), você definiria apenas as assinaturas
    // da interface e o MapStruct implementaria isso.
}
