package com.smarttruck.presentation.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smarttruck.application.usecase.CreateUserUseCase;
import com.smarttruck.config.SecurityConfig;
import com.smarttruck.domain.model.User;
import com.smarttruck.presentation.dto.CreateUserRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Import(SecurityConfig.class)
@WebMvcTest(UserController.class)
class UserControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CreateUserUseCase createUserUseCase;

    @Autowired
    private ObjectMapper objectMapper;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User("Alice", "1234567890", "alice@example.com", "hashedPass");
    }

    @Test
    void shouldReturn200AndResponseBody_whenRequestIsValid() throws Exception {
        // Arrange
        CreateUserRequest request = new CreateUserRequest();
        request.setName("Alice");
        request.setEmail("alice@example.com");
        request.setPassword("password123");
        request.setPhone("1234567890");

        when(createUserUseCase.execute(request.getName(), request.getEmail(), request.getPassword(),
            request.getPhone())).thenReturn(user);

        // Act & Assert
        mockMvc.perform(post("/api/users").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))).andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(user.getId()))
            .andExpect(jsonPath("$.name").value(user.getName()))
            .andExpect(jsonPath("$.email").value(user.getEmail()))
            .andExpect(jsonPath("$.phone").value(user.getPhone()));
    }

    @Test
    void shouldReturn400_whenRequestIsInvalid() throws Exception {
        // Arrange: request sem campos obrigatórios
        CreateUserRequest invalidRequest = new CreateUserRequest();

        // Act & Assert
        mockMvc.perform(post("/api/users").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequest)))
            .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturn400_whenUseCaseReturnsNull() throws Exception {
        // Arrange
        CreateUserRequest request = new CreateUserRequest();
        request.setName("Bob");
        request.setEmail("bob@example.com");
        request.setPassword("pass123");
        request.setPhone("0987654321");

        when(createUserUseCase.execute(request.getName(), request.getEmail(), request.getPassword(),
            request.getPhone())).thenReturn(null);

        // Act & Assert
        mockMvc.perform(post("/api/users").contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request))).andExpect(status().isBadRequest());
    }
}
