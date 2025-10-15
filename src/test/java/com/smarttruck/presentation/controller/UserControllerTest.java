package com.smarttruck.presentation.controller;

import com.smarttruck.application.usecase.CreateUserUseCase;
import com.smarttruck.domain.model.User;
import com.smarttruck.presentation.dto.CreateUserRequest;
import com.smarttruck.presentation.dto.CreateUserResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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
        CreateUserRequest request = new CreateUserRequest();
        request.setName("Alice");
        request.setEmail("alice@example.com");
        request.setPassword("password123");
        request.setPhone("1234567890");

        User user = new User("Alice", "1234567890", "alice@example.com", "hashedPassword");

        when(createUserUseCase.execute(request.getName(), request.getEmail(), request.getPassword(),
            request.getPhone())).thenReturn(user);

        // Act
        ResponseEntity<CreateUserResponse> response = userController.create(request);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(user.getName(), response.getBody().getName());
        assertEquals(user.getEmail(), response.getBody().getEmail());
        assertEquals(user.getPhone(), response.getBody().getPhone());

        // Verifica que o use case foi chamado com os parâmetros corretos
        verify(createUserUseCase).execute(request.getName(), request.getEmail(),
            request.getPassword(), request.getPhone());
    }

    @Test
    void create_shouldReturn400WhenUserIsNull() {
        // Arrange
        CreateUserRequest request = new CreateUserRequest();
        request.setName("Bob");
        request.setEmail("bob@example.com");
        request.setPassword("pass123");
        request.setPhone("0987654321");

        when(createUserUseCase.execute(anyString(), anyString(), anyString(),
            anyString())).thenReturn(null);

        // Act
        ResponseEntity<CreateUserResponse> response = userController.create(request);

        // Assert
        assertEquals(400, response.getStatusCodeValue());
        assertNull(response.getBody());

        verify(createUserUseCase).execute(request.getName(), request.getEmail(),
            request.getPassword(), request.getPhone());
    }

    @Test
    void create_shouldCallUseCaseWithCorrectArguments() {
        // Arrange
        CreateUserRequest request = new CreateUserRequest();
        request.setName("Charlie");
        request.setEmail("charlie@example.com");
        request.setPassword("mypassword");
        request.setPhone("5555555555");

        User user = new User("Charlie", "5555555555", "charlie@example.com", "hashedPass");
        when(createUserUseCase.execute(anyString(), anyString(), anyString(),
            anyString())).thenReturn(user);

        ArgumentCaptor<String> nameCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> emailCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> passwordCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> phoneCaptor = ArgumentCaptor.forClass(String.class);

        // Act
        userController.create(request);

        // Assert
        verify(createUserUseCase).execute(nameCaptor.capture(), emailCaptor.capture(),
            passwordCaptor.capture(), phoneCaptor.capture());

        assertEquals(request.getName(), nameCaptor.getValue());
        assertEquals(request.getEmail(), emailCaptor.getValue());
        assertEquals(request.getPassword(), passwordCaptor.getValue());
        assertEquals(request.getPhone(), phoneCaptor.getValue());
    }
}
