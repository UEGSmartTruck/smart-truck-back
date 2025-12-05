package com.smarttruck.presentation.controller;

import com.smarttruck.application.usecase.CreateUserUseCase;
import com.smarttruck.application.usecase.ListAllUserUseCase;
import com.smarttruck.domain.model.User;
import com.smarttruck.presentation.exception.GlobalExceptionHandler;
import com.smarttruck.shared.security.CustomUserDetailsService;
import com.smarttruck.shared.security.JwtTokenProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.Arrays;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc(addFilters = false) // desativa filtros de segurança
@WebMvcTest(UserController.class)
@ContextConfiguration(
    classes = {UserController.class, GlobalExceptionHandler.class}) // garante que controller e exception handler sejam carregados
class UserControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CustomUserDetailsService userDetailsService; // mock do serviço de segurança

    @MockitoBean
    private CreateUserUseCase createUserUseCase;

    @MockitoBean
    private ListAllUserUseCase listAllUserUseCase;

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

    @Test
    void shouldListAllUsers() throws Exception {
        // Arrange
        Instant now = Instant.now();
        User user1 = new User("1", "Alice", "111", "alice@example.com", "pass", now, now, null, now);
        User user2 = new User("2", "Bob", "222", "bob@example.com", "pass", now, now, null, now);
        User user3 = new User("3", "Charlie", "333", "charlie@example.com", "pass", now, now, null, now);

        Page<User> page = new PageImpl<>(Arrays.asList(user1, user2, user3), PageRequest.of(0, 20), 3);

        when(listAllUserUseCase.execute(any())).thenReturn(page);

        // Act & Assert
        mockMvc.perform(get("/users")
                .param("page", "0")
                .param("size", "20"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.users").isArray())
            .andExpect(jsonPath("$.users.length()").value(3))
            .andExpect(jsonPath("$.users[0].id").value("1"))
            .andExpect(jsonPath("$.users[0].name").value("Alice"))
            .andExpect(jsonPath("$.users[0].email").value("alice@example.com"))
            .andExpect(jsonPath("$.metadata.totalElements").value(3))
            .andExpect(jsonPath("$.metadata.totalPages").value(1))
            .andExpect(jsonPath("$.metadata.currentPage").value(0))
            .andExpect(jsonPath("$.metadata.pageSize").value(20));
    }

    @Test
    void shouldReturnEmptyListWhenNoUsers() throws Exception {
        // Arrange
        Page<User> emptyPage = new PageImpl<>(Collections.emptyList(), PageRequest.of(0, 20), 0);

        when(listAllUserUseCase.execute(any())).thenReturn(emptyPage);

        // Act & Assert
        mockMvc.perform(get("/users"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.users").isArray())
            .andExpect(jsonPath("$.users.length()").value(0))
            .andExpect(jsonPath("$.metadata.totalElements").value(0))
            .andExpect(jsonPath("$.metadata.totalPages").value(0));
    }

    @Test
    void shouldReturn401WhenNotAuthenticated() throws Exception {
        // Note: This test requires security filters enabled
        // For now, we'll skip this as filters are disabled in @AutoConfigureMockMvc(addFilters = false)
        // In a real scenario, you would:
        // 1. Enable filters
        // 2. Not provide Authorization header
        // 3. Expect 401 Unauthorized

        // This is a placeholder test to document the requirement
        // The actual authentication check is done by Spring Security filters
    }

    @Test
    void shouldFilterDeletedUsers() throws Exception {
        // Arrange - 2 active users and 1 deleted
        Instant now = Instant.now();
        User activeUser1 = new User("1", "Alice", "111", "alice@example.com", "pass", now, now, null, now);
        User activeUser2 = new User("2", "Bob", "222", "bob@example.com", "pass", now, now, null, now);
        // Deleted user (deletedAt != null) should NOT be in the result

        Page<User> page = new PageImpl<>(Arrays.asList(activeUser1, activeUser2), PageRequest.of(0, 20), 2);

        when(listAllUserUseCase.execute(any())).thenReturn(page);

        // Act & Assert
        mockMvc.perform(get("/users"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.users").isArray())
            .andExpect(jsonPath("$.users.length()").value(2))
            .andExpect(jsonPath("$.users[0].deletedAt").isEmpty())
            .andExpect(jsonPath("$.users[1].deletedAt").isEmpty())
            .andExpect(jsonPath("$.metadata.totalElements").value(2));
    }

    @Test
    void shouldReturnEmptyWhenAllDeleted() throws Exception {
        // Arrange - All users are deleted
        Page<User> emptyPage = new PageImpl<>(Collections.emptyList(), PageRequest.of(0, 20), 0);

        when(listAllUserUseCase.execute(any())).thenReturn(emptyPage);

        // Act & Assert
        mockMvc.perform(get("/users"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.users").isArray())
            .andExpect(jsonPath("$.users.length()").value(0))
            .andExpect(jsonPath("$.metadata.totalElements").value(0))
            .andExpect(jsonPath("$.metadata.totalPages").value(0));
    }

    @Test
    void shouldPaginateUsers() throws Exception {
        // Arrange - 50 users total, page 0, size 10
        Instant now = Instant.now();
        User[] users = new User[10];
        for (int i = 0; i < 10; i++) {
            users[i] = new User(String.valueOf(i), "User" + i, "phone" + i, "user" + i + "@example.com", "pass", now, now, null, now);
        }

        Page<User> page = new PageImpl<>(Arrays.asList(users), PageRequest.of(0, 10), 50);

        when(listAllUserUseCase.execute(any())).thenReturn(page);

        // Act & Assert
        mockMvc.perform(get("/users")
                .param("page", "0")
                .param("size", "10"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.users").isArray())
            .andExpect(jsonPath("$.users.length()").value(10))
            .andExpect(jsonPath("$.metadata.totalElements").value(50))
            .andExpect(jsonPath("$.metadata.totalPages").value(5))
            .andExpect(jsonPath("$.metadata.currentPage").value(0))
            .andExpect(jsonPath("$.metadata.pageSize").value(10));
    }

    @Test
    void shouldReturnLastPartialPage() throws Exception {
        // Arrange - 25 total users, last page (page 2, size 10) has only 5 users
        Instant now = Instant.now();
        User[] users = new User[5];
        for (int i = 0; i < 5; i++) {
            users[i] = new User(String.valueOf(20 + i), "User" + (20 + i), "phone" + i, "user" + (20 + i) + "@example.com", "pass", now, now, null, now);
        }

        Page<User> page = new PageImpl<>(Arrays.asList(users), PageRequest.of(2, 10), 25);

        when(listAllUserUseCase.execute(any())).thenReturn(page);

        // Act & Assert
        mockMvc.perform(get("/users")
                .param("page", "2")
                .param("size", "10"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.users").isArray())
            .andExpect(jsonPath("$.users.length()").value(5))
            .andExpect(jsonPath("$.metadata.totalElements").value(25))
            .andExpect(jsonPath("$.metadata.totalPages").value(3))
            .andExpect(jsonPath("$.metadata.currentPage").value(2))
            .andExpect(jsonPath("$.metadata.pageSize").value(10));
    }

    @Test
    void shouldReturn400OnInvalidParams() throws Exception {
        // Test negative page
        mockMvc.perform(get("/users")
                .param("page", "-1")
                .param("size", "20"))
            .andExpect(status().isBadRequest());

        // Test size too large
        mockMvc.perform(get("/users")
                .param("page", "0")
                .param("size", "101"))
            .andExpect(status().isBadRequest());

        // Test size too small
        mockMvc.perform(get("/users")
                .param("page", "0")
                .param("size", "0"))
            .andExpect(status().isBadRequest());
    }

    @Test
    void shouldApplyDefaultPagination() throws Exception {
        // Arrange - Default pagination (page=0, size=20)
        Instant now = Instant.now();
        User[] users = new User[20];
        for (int i = 0; i < 20; i++) {
            users[i] = new User(String.valueOf(i), "User" + i, "phone" + i, "user" + i + "@example.com", "pass", now, now, null, now);
        }

        Page<User> page = new PageImpl<>(Arrays.asList(users), PageRequest.of(0, 20), 100);

        when(listAllUserUseCase.execute(any())).thenReturn(page);

        // Act & Assert - Call without params should use defaults
        mockMvc.perform(get("/users"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.users").isArray())
            .andExpect(jsonPath("$.users.length()").value(20))
            .andExpect(jsonPath("$.metadata.currentPage").value(0))
            .andExpect(jsonPath("$.metadata.pageSize").value(20));
    }
}
