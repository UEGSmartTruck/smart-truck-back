package com.smarttruck.presentation.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.smarttruck.domain.model.User;
import com.smarttruck.domain.repository.UserRepository;
import com.smarttruck.presentation.dto.LoginRequest;
import com.smarttruck.presentation.dto.TokenResponse;
import com.smarttruck.shared.security.JwtTokenProvider;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class LoginControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Test
    void login_shouldAuthenticateAndReturnToken() throws Exception {
        // Arrange
        final String email = "test@example.com";
        final String password = "password123";
        final String hashedPassword = passwordEncoder.encode(password);

        final User user = new User("Test User", "1234567890", email, hashedPassword);
        userRepository.save(user);

        final LoginRequest loginRequest = new LoginRequest(email, password);

        // Act
        final MvcResult result = mockMvc
                .perform(post("/auth/login").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk()).andReturn();

        // Assert
        final TokenResponse response = objectMapper
                .readValue(result.getResponse().getContentAsString(), TokenResponse.class);

        assertNotNull(response.accessToken());
        assertTrue(jwtTokenProvider.validateToken(response.accessToken()));
        assertEquals(email, jwtTokenProvider.getEmailFromToken(response.accessToken()));
    }

    @Test
    void login_shouldReturn401ForInvalidCredentials() throws Exception {
        // Arrange
        final LoginRequest loginRequest = new LoginRequest("invalid@example.com", "wrongpassword");

        // Act & Assert
        mockMvc.perform(post("/auth/login").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void logout_shouldInvalidateToken() throws Exception {
        // Arrange
        final String email = "logout@example.com";
        final String password = "password123";
        final String hashedPassword = passwordEncoder.encode(password);

        final User user = new User("Logout Test", "1234567890", email, hashedPassword);
        userRepository.save(user);

        // Login first to get a token
        final LoginRequest loginRequest = new LoginRequest(email, password);
        final MvcResult loginResult = mockMvc
                .perform(post("/auth/login").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk()).andReturn();

        final TokenResponse tokenResponse = objectMapper
                .readValue(loginResult.getResponse().getContentAsString(), TokenResponse.class);

        // Act & Assert
        mockMvc.perform(post("/auth/logout").header("Authorization",
                "Bearer " + tokenResponse.accessToken())).andExpect(status().isOk());

        // Verify token is invalid after logout
        assertFalse(jwtTokenProvider.validateToken(tokenResponse.accessToken()));
    }
}
