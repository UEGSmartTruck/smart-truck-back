package com.smarttruck.presentation.controller;

import com.smarttruck.application.usecase.AuthenticateUserUseCase;
import com.smarttruck.presentation.dto.LoginRequest;
import com.smarttruck.presentation.dto.LoginResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/")
public class LoginController {

    private final AuthenticateUserUseCase authenticateUserUseCase;

    public LoginController(AuthenticateUserUseCase authenticateUserUseCase) {
        this.authenticateUserUseCase = authenticateUserUseCase;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        String token = authenticateUserUseCase.execute(request.email(), request.password());
        return ResponseEntity.ok(new LoginResponse(token));
    }
}
