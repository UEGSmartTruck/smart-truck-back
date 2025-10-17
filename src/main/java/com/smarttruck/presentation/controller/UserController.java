package com.smarttruck.presentation.controller;

import com.smarttruck.application.usecase.CreateUserUseCase;
import com.smarttruck.application.usecase.FindAllUsersUseCase; // <-- 1. Importe o novo UseCase
import com.smarttruck.presentation.dto.CreateUserRequest;
import com.smarttruck.presentation.dto.CreateUserResponse;
import com.smarttruck.presentation.dto.UserResponse; // <-- 2. Importe o novo DTO
import com.smarttruck.presentation.mapper.UserMapper;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*; // Importe GetMapping

import java.util.List; // Importe List

@RestController
@RequestMapping("/users")
@Validated
public class UserController {

    // SEU CÓDIGO ORIGINAL (mantido)
    private final CreateUserUseCase createUserUseCase;

    // --- CÓDIGO ADICIONADO ---
    private final FindAllUsersUseCase findAllUsersUseCase; // <-- 3. Adicione o novo UseCase como dependência

    // --- CONSTRUTOR ATUALIZADO ---
    // 4. Atualize o construtor para receber as duas dependências
    public UserController(CreateUserUseCase createUserUseCase, FindAllUsersUseCase findAllUsersUseCase) {
        this.createUserUseCase = createUserUseCase;
        this.findAllUsersUseCase = findAllUsersUseCase;
    }
    // --- FIM DA ATUALIZAÇÃO ---


    // SEU MÉTODO ORIGINAL (intacto)
    @PostMapping
    public ResponseEntity<CreateUserResponse> create(
        @Valid @RequestBody CreateUserRequest request) {
        var user =
            createUserUseCase.execute(request.name(), request.email(), request.password(),
                request.phone());

        if (user == null) {
            return ResponseEntity.badRequest().build();
        }

        // Observação: seu UserMapper precisará de um método toResponse para o CreateUserResponse
        // e outro (toResponseList) para a lista de usuários.
        CreateUserResponse response = UserMapper.toResponse(user); // Assumindo que este método existe e funciona
        return ResponseEntity.ok(response);
    }

    // --- NOVO MÉTODO ADICIONADO ---
    /**
     * Retorna uma lista de todos os usuários cadastrados.
     * @return {@link List<UserResponse>} com os dados dos usuários
     */
    @GetMapping // <-- 5. Crie o novo método com a anotação @GetMapping
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        List<UserResponse> users = findAllUsersUseCase.execute();
        return ResponseEntity.ok(users);
    }
    // --- FIM DO NOVO MÉTODO ---
}
