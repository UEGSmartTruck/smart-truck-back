package com.smarttruck.infrastructure.persistence;

import com.smarttruck.domain.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class JpaUserRepositoryTest {

    private SpringDataJpaUserRepository springRepo;
    private JpaUserMapper mapper;
    private JpaUserRepository jpaUserRepository;

    @BeforeEach
    void setUp() {
        springRepo = mock(SpringDataJpaUserRepository.class);
        mapper = mock(JpaUserMapper.class);
        jpaUserRepository = new JpaUserRepository(springRepo, mapper);
    }

    @Test
    void save_shouldCallMapperAndRepositoryAndReturnDomain() {
        // Arrange
        User domainUser = new User("Alice", "1234567890", "alice@example.com", "hashedPass");
        JpaUser entityUser = new JpaUser();
        JpaUser savedEntity = new JpaUser();

        when(mapper.toEntity(domainUser)).thenReturn(entityUser);
        when(springRepo.save(entityUser)).thenReturn(savedEntity);
        when(mapper.toDomain(savedEntity)).thenReturn(domainUser);

        // Act
        User result = jpaUserRepository.save(domainUser);

        // Assert
        assertNotNull(result);
        assertEquals(domainUser, result);

        verify(mapper).toEntity(domainUser);
        verify(springRepo).save(entityUser);
        verify(mapper).toDomain(savedEntity);
    }

    @Test
    void existsByEmail_shouldDelegateToSpringRepo() {
        // Arrange
        String email = "bob@example.com";
        when(springRepo.existsByEmail(email)).thenReturn(true);

        // Act
        boolean result = jpaUserRepository.existsByEmail(email);

        // Assert
        assertTrue(result);
        verify(springRepo).existsByEmail(email);
    }

    @Test
    void save_shouldCaptureEntityPassedToSpringRepo() {
        User domainUser = new User("Charlie", "5555555555", "charlie@example.com", "hashedPass");
        JpaUser entityUser = new JpaUser();
        JpaUser savedEntity = new JpaUser();

        when(mapper.toEntity(domainUser)).thenReturn(entityUser);
        when(springRepo.save(any(JpaUser.class))).thenReturn(savedEntity);
        when(mapper.toDomain(savedEntity)).thenReturn(domainUser);

        ArgumentCaptor<JpaUser> captor = ArgumentCaptor.forClass(JpaUser.class);

        jpaUserRepository.save(domainUser);

        verify(springRepo).save(captor.capture());
        assertEquals(entityUser, captor.getValue());
    }
}
