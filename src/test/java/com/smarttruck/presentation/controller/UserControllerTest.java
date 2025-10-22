package com.smarttruck.presentation.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.http.ResponseEntity;
import com.smarttruck.application.usecase.CreateUserUseCase;
import com.smarttruck.domain.model.User;
import com.smarttruck.presentation.dto.CreateUserRequest;
import com.smarttruck.presentation.dto.CreateUserResponse;

class UserControllerTest {

    private CreateUserUseCase createUserUseCase;
    private UserController userController;

    @BeforeEach
    void setUp() {
        createUserUseCase = mock(CreateUserUseCase.class);
        userController = new UserController(createUserUseCase);
    }

    @Test
    void create_shouldReturn200AndResponseWhenUserCreated() {
        // Arrange
        final CreateUserRequest request =
                new CreateUserRequest("Alice", "alice@example.com", "password123", "1234567890");

        final User user = new User("Alice", "1234567890", "alice@example.com", "hashedPassword");

        when(createUserUseCase.execute(request.name(), request.email(), request.password(),
                request.phone())).thenReturn(user);

        // Act
        final ResponseEntity<CreateUserResponse> response = userController.create(request);

        // Assert
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(user.getName(), response.getBody().name());
        assertEquals(user.getEmail(), response.getBody().email());
        assertEquals(user.getPhone(), response.getBody().phone());

        // Verifica que o use case foi chamado com os parâmetros corretos
        verify(createUserUseCase).execute(request.name(), request.email(), request.password(),
                request.phone());
    }

    @Test
    void create_shouldReturn400WhenUserIsNull() {
        // Arrange
        final CreateUserRequest request =
                new CreateUserRequest("Bob", "bob@example.com", "pass123", "0987654321");

        when(createUserUseCase.execute(anyString(), anyString(), anyString(), anyString()))
                .thenReturn(null);

        // Act
        final ResponseEntity<CreateUserResponse> response = userController.create(request);

        // Assert
        assertEquals(400, response.getStatusCode().value());
        assertNull(response.getBody());

        verify(createUserUseCase).execute(request.name(), request.email(), request.password(),
                request.phone());
    }

    @Test
    void create_shouldCallUseCaseWithCorrectArguments() {
        // Arrange
        final CreateUserRequest request =
                new CreateUserRequest("Charlie", "charlie@example.com", "mypassword", "5555555555");

        final User user = new User("Charlie", "5555555555", "charlie@example.com", "hashedPass");
        when(createUserUseCase.execute(anyString(), anyString(), anyString(), anyString()))
                .thenReturn(user);

        final ArgumentCaptor<String> nameCaptor = ArgumentCaptor.forClass(String.class);
        final ArgumentCaptor<String> emailCaptor = ArgumentCaptor.forClass(String.class);
        final ArgumentCaptor<String> passwordCaptor = ArgumentCaptor.forClass(String.class);
        final ArgumentCaptor<String> phoneCaptor = ArgumentCaptor.forClass(String.class);

        // Act
        userController.create(request);

        // Assert
        verify(createUserUseCase).execute(nameCaptor.capture(), emailCaptor.capture(),
                passwordCaptor.capture(), phoneCaptor.capture());

        assertEquals(request.name(), nameCaptor.getValue());
        assertEquals(request.email(), emailCaptor.getValue());
        assertEquals(request.password(), passwordCaptor.getValue());
        assertEquals(request.phone(), phoneCaptor.getValue());
    }
}
