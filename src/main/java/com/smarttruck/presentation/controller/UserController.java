package com.smarttruck.presentation.controller;


import com.smarttruck.application.usecase.CreateUserUseCase;
import com.smarttruck.application.usecase.ListAllUserUseCase;
import com.smarttruck.domain.model.User;
import com.smarttruck.presentation.dto.CreateUserRequest;
import com.smarttruck.presentation.dto.CreateUserResponse;
import com.smarttruck.presentation.dto.ListAllUserResponse;
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


@RestController
@RequestMapping("/users")
@Validated
public class UserController {


    private final CreateUserUseCase createUserUseCase;
    private final ListAllUserUseCase listAllUserUseCase;

    public UserController(CreateUserUseCase createUserUseCase, ListAllUserUseCase listAllUserUseCase) {
        this.createUserUseCase = createUserUseCase;
        this.listAllUserUseCase = listAllUserUseCase;
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
    /*
    @PutMapping("/{id}")
    public ResponseEntity<CreateUserResponse> update(
        @PathVariable UUID id,
        @Valid @RequestBody UpdateUserRequest request) {

        // Chama o caso de uso para atualizar
        User updatedUser = updateUserUseCase.execute(
            id,
            request.name(),
            request.email(),
            request.phone()
        );

         */
    /**
     * Lista usuários ativos com paginação.
     *
     * @param page número da página (default: 0, min: 0)
     * @param size tamanho da página (default: 20, min: 1, max: 100)
     * @return ResponseEntity com ListAllUserResponse contendo usuários e metadados de paginação
     */
    @GetMapping
    public ResponseEntity<ListAllUserResponse> findAll(
        @RequestParam(defaultValue = "0") @Min(0) int page,
        @RequestParam(defaultValue = "20") @Min(1) @Max(100) int size
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<User> userPage = listAllUserUseCase.execute(pageable);
        ListAllUserResponse response = UserMapper.toListAllResponse(userPage);
        return ResponseEntity.ok(response);
    }
}
