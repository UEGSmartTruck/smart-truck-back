package com.smarttruck.presentation.controller;

import com.smarttruck.application.usecase.CreateUserUseCase;
import com.smarttruck.application.usecase.ListAllUsersUseCase; // 1. Importar o novo UseCase
import com.smarttruck.domain.model.User;
import com.smarttruck.presentation.dto.CreateUserRequest;
import com.smarttruck.presentation.dto.CreateUserResponse;
import com.smarttruck.presentation.dto.UserResponse; // 2. Importar o DTO de resposta
import com.smarttruck.presentation.mapper.UserMapper;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*; // 3. Importar GetMapping

import java.util.List; // 4. Importar List

@RestController
@RequestMapping("/auth/users") // O caminho base é /users
@Validated
public class UserController {

    private final CreateUserUseCase createUserUseCase;
    private final ListAllUsersUseCase listAllUsersUseCase; // 5. Injetar o novo UseCase

    // 6. Atualizar o construtor
    public UserController(CreateUserUseCase createUserUseCase, ListAllUsersUseCase listAllUsersUseCase) {
        this.createUserUseCase = createUserUseCase;
        this.listAllUsersUseCase = listAllUsersUseCase;
    }

    @PostMapping
    public ResponseEntity<CreateUserResponse> create(
        @Valid @RequestBody CreateUserRequest request) {
        var user =
            createUserUseCase.execute(request.name(), request.email(), request.password(),
                request.phone());

        if (user == null) {
            return ResponseEntity.badRequest().build();
        }

        CreateUserResponse response = UserMapper.toResponse(user);
        return ResponseEntity.ok(response);
    }

    // 7. Adicionar o novo endpoint
    /**
     * Endpoint para listar todos os usuários.
     * Requer autenticação (definido no SecurityConfig).
     */
    @GetMapping
    public ResponseEntity<List<UserResponse>> listAll() {
        // 1. Chama o caso de uso
        List<User> users = listAllUsersUseCase.execute();

        // 2. Mapeia a lista de domínio para a lista de DTO de resposta
        List<UserResponse> response = UserMapper.toUserResponseList(users);

        // 3. Retorna 200 OK com a lista no corpo
        return ResponseEntity.ok(response);
    }
}
