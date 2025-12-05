package com.smarttruck.application.service;

import com.smarttruck.domain.model.User;
import com.smarttruck.domain.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.Instant;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ListAllUserServiceTest {

    private UserRepository userRepository;
    private ListAllUserService listAllUserService;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        listAllUserService = new ListAllUserService(userRepository);
    }

    @Test
    void shouldReturnPageOfUsers() {
        // Arrange
        Instant now = Instant.now();
        User user1 = new User("1", "Alice", "111", "alice@example.com", "pass", now, now, null, now);
        User user2 = new User("2", "Bob", "222", "bob@example.com", "pass", now, now, null, now);
        User user3 = new User("3", "Charlie", "333", "charlie@example.com", "pass", now, now, null, now);

        List<User> users = Arrays.asList(user1, user2, user3);
        Pageable pageable = PageRequest.of(0, 20);
        Page<User> expectedPage = new PageImpl<>(users, pageable, users.size());

        when(userRepository.findAllActive(pageable)).thenReturn(expectedPage);

        // Act
        Page<User> result = listAllUserService.execute(pageable);

        // Assert
        assertNotNull(result);
        assertEquals(3, result.getContent().size());
        assertEquals(3, result.getTotalElements());
        assertEquals(1, result.getTotalPages());
        assertEquals(0, result.getNumber());
        assertEquals(20, result.getSize());

        verify(userRepository).findAllActive(pageable);
    }

    @Test
    void shouldReturnEmptyPageWhenNoUsers() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 20);
        Page<User> emptyPage = new PageImpl<>(Collections.emptyList(), pageable, 0);

        when(userRepository.findAllActive(pageable)).thenReturn(emptyPage);

        // Act
        Page<User> result = listAllUserService.execute(pageable);

        // Assert
        assertNotNull(result);
        assertTrue(result.getContent().isEmpty());
        assertEquals(0, result.getTotalElements());
        assertEquals(0, result.getTotalPages());

        verify(userRepository).findAllActive(pageable);
    }
}
