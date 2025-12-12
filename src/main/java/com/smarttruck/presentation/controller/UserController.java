package com.smarttruck.presentation.controller;


import com.smarttruck.application.usecase.CreateUserUseCase;
import com.smarttruck.application.usecase.DeleteUserUseCase;
import com.smarttruck.application.usecase.ListAllUserUseCase;
import com.smarttruck.application.usecase.UpdateUserUseCase;
import com.smarttruck.domain.model.User;
import com.smarttruck.presentation.dto.CreateUserRequest;
import com.smarttruck.presentation.dto.CreateUserResponse;
import com.smarttruck.presentation.dto.ListAllUserResponse;
import com.smarttruck.presentation.dto.UpdateUserRequest;
import com.smarttruck.presentation.mapper.UserMapper;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;


@RestController
@RequestMapping("/users")
@Validated
public class UserController {


    private final CreateUserUseCase createUserUseCase;
    private final ListAllUserUseCase listAllUserUseCase;
    private final UpdateUserUseCase updateUserUseCase;
    private final DeleteUserUseCase deleteUserUseCase;
    public UserController(CreateUserUseCase createUserUseCase, ListAllUserUseCase listAllUserUseCase, UpdateUserUseCase updateUserUseCase, DeleteUserUseCase deleteUserUseCase) {
        this.createUserUseCase = createUserUseCase;
        this.listAllUserUseCase = listAllUserUseCase;
        this.updateUserUseCase = updateUserUseCase;
        this.deleteUserUseCase = deleteUserUseCase;
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

    /**
     * Lista usuários ativos com paginação.
     *
     * @param page número da página (default: 0, min: 0)
     * @param size tamanho da página (default: 20, min: 1, max: 100)
     * @return ResponseEntity com ListAllUserResponse contendo usuários e metadados de paginação
     */
    @GetMapping
    public ResponseEntity<ListAllUserResponse> findAll(
        @RequestParam(name = "page", defaultValue = "0") @Min(0) int page,
        @RequestParam(name = "size", defaultValue = "20") @Min(1) @Max(100) int size
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<User> userPage = listAllUserUseCase.execute(pageable);
        ListAllUserResponse response = UserMapper.toListAllResponse(userPage);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CreateUserResponse> update(
        @PathVariable UUID id,
        @Valid @RequestBody UpdateUserRequest request) {

        // Executa a atualização (Nome, Email, Telefone)
        // O ID vem da URL e os dados vêm do corpo da requisição (JSON do Angular)
        var updatedUser = updateUserUseCase.execute(
            id,
            request.name(),
            request.email(),
            request.phone()
        );

        if (updatedUser == null) {
            return ResponseEntity.notFound().build();
        }

        // Retornamos os dados atualizados para o Front atualizar a lista
        CreateUserResponse response = UserMapper.toResponse(updatedUser);
        return ResponseEntity.ok(response);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        deleteUserUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }

}

