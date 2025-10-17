package com.smarttruck.presentation.controller;

import com.smarttruck.application.usecase.CreateUserUseCase;
import com.smarttruck.domain.model.User;
import com.smarttruck.shared.security.CustomUserDetailsService;
import com.smarttruck.shared.security.JwtTokenProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc(addFilters = false) // desativa filtros de segurança
@WebMvcTest(UserController.class)
@ContextConfiguration(
    classes = {UserController.class}) // garante que apenas o controller seja carregado
class UserControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CustomUserDetailsService userDetailsService; // mock do serviço de segurança

    @MockitoBean
    private CreateUserUseCase createUserUseCase;

    @MockitoBean
    private JwtTokenProvider jwtTokenProvider;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User("Alice", "1234567890", "alice@example.com", "hashedPass");
    }

    @Test
    void shouldReturn200AndResponseBody_whenRequestIsValid() throws Exception {
        String requestJson = """
                {
                  "name": "Alice",
                  "email": "alice@example.com",
                  "password": "password123",
                  "phone": "1234567890"
                }
            """;

        when(createUserUseCase.execute("Alice", "alice@example.com", "password123",
            "1234567890")).thenReturn(user);

        mockMvc.perform(post("/users").contentType(MediaType.APPLICATION_JSON).content(requestJson))
            .andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(user.getId()))
            .andExpect(jsonPath("$.name").value(user.getName()))
            .andExpect(jsonPath("$.email").value(user.getEmail()))
            .andExpect(jsonPath("$.phone").value(user.getPhone()));
    }

    @Test
    void shouldReturn400_whenRequestIsInvalid() throws Exception {
        String invalidJson = "{}";

        mockMvc.perform(post("/users").contentType(MediaType.APPLICATION_JSON).content(invalidJson))
            .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturn400_whenUseCaseReturnsNull() throws Exception {
        String requestJson = """
                {
                  "name": "Bob",
                  "email": "bob@example.com",
                  "password": "pass123",
                  "phone": "0987654321"
                }
            """;

        when(createUserUseCase.execute("Bob", "bob@example.com", "pass123",
            "0987654321")).thenReturn(null);

        mockMvc.perform(post("/users").contentType(MediaType.APPLICATION_JSON).content(requestJson))
            .andExpect(status().isBadRequest());
    }
}
