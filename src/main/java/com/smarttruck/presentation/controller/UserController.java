package com.smarttruck.presentation.controller;


import com.smarttruck.application.usecase.CreateUserUseCase;
import com.smarttruck.presentation.dto.CreateUserRequest;
import com.smarttruck.presentation.dto.CreateUserResponse;
import com.smarttruck.presentation.mapper.UserMapper;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/users")
@Validated
public class UserController {


    private final CreateUserUseCase createUserUseCase;


    public UserController(CreateUserUseCase createUserUseCase) {
        this.createUserUseCase = createUserUseCase;
    }


    @PostMapping
    public ResponseEntity<CreateUserResponse> create(
        @Valid @RequestBody CreateUserRequest request) {
        var user =
            createUserUseCase.execute(request.getName(), request.getEmail(), request.getPassword(),
                request.getPhone());

        if (user == null) {
            return ResponseEntity.badRequest().build();
        }

        CreateUserResponse response = UserMapper.toResponse(user);
        return ResponseEntity.ok(response);
    }
}
