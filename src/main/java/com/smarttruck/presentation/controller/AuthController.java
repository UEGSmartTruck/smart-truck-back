// src/main/java/com/smarttruck/presentation/controller/AuthController.java
package com.smarttruck.presentation.controller;

import com.smarttruck.application.usecase.AuthenticateUserUseCase;
import com.smarttruck.presentation.dto.LoginRequest;
import com.smarttruck.presentation.dto.UserResponse;
import com.smarttruck.presentation.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthenticateUserUseCase authenticateUserUseCase;
    private final UserMapper userMapper;

    @PostMapping("/login")
    public ResponseEntity<UserResponse> login(@RequestBody LoginRequest loginRequest) {
        return authenticateUserUseCase.execute(loginRequest)
            .map(userMapper::toResponse) // Mapeia o User do domínio para UserResponse
            .map(ResponseEntity::ok)
            .orElseGet(() -> ResponseEntity.status(401).build());
    }
}
