package com.smarttruck.application.service;

import com.smarttruck.domain.model.User;
import com.smarttruck.domain.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CreateUserServiceTest {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private CreateUserService createUserService;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        passwordEncoder = mock(PasswordEncoder.class);
        createUserService = new CreateUserService(userRepository, passwordEncoder);
    }

    @Test
    void execute_shouldCreateUserSuccessfully() {
        // Cenário: email não existe
        String name = "Alice";
        String email = "alice@example.com";
        String password = "password123";
        String phone = "1234567890";

        when(userRepository.existsByEmail(email)).thenReturn(false);
        when(passwordEncoder.encode(password)).thenReturn("hashedPassword");

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        when(userRepository.save(userCaptor.capture())).thenAnswer(
            invocation -> invocation.getArgument(0));

        User result = createUserService.execute(name, email, password, phone);

        // Validações
        verify(userRepository).existsByEmail(email);
        verify(passwordEncoder).encode(password);
        verify(userRepository).save(any(User.class));

        assertNotNull(result);
        assertEquals(name, result.getName());
        assertEquals(email, result.getEmail());
        assertEquals(phone, result.getPhone());
        assertEquals("hashedPassword", result.getPasswordHash());

        // Confirma que o objeto salvo no repositório tem a senha codificada
        User savedUser = userCaptor.getValue();
        assertEquals("hashedPassword", savedUser.getPasswordHash());
    }

    @Test
    void execute_shouldThrowExceptionWhenEmailAlreadyExists() {
        // Cenário: email já existe
        String email = "bob@example.com";
        when(userRepository.existsByEmail(email)).thenReturn(true);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
            () -> createUserService.execute("Bob", email, "password", "0987654321"));

        assertEquals("Email already in use", exception.getMessage());

        // Garantia de que save e encode não foram chamados
        verify(userRepository).existsByEmail(email);
        verify(userRepository, never()).save(any());
        verify(passwordEncoder, never()).encode(any());
    }

    @Test
    void execute_shouldPassCorrectUserToRepository() {
        // Cenário detalhado: captura todos os atributos do usuário salvo
        String name = "Charlie";
        String email = "charlie@example.com";
        String password = "mypassword";
        String phone = "5555555555";

        when(userRepository.existsByEmail(email)).thenReturn(false);
        when(passwordEncoder.encode(password)).thenReturn("encodedPassword");

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        when(userRepository.save(captor.capture())).thenAnswer(
            invocation -> invocation.getArgument(0));

        createUserService.execute(name, email, password, phone);

        User captured = captor.getValue();
        assertEquals(name, captured.getName());
        assertEquals(email, captured.getEmail());
        assertEquals(phone, captured.getPhone());
        assertEquals("encodedPassword", captured.getPasswordHash());
    }
}
