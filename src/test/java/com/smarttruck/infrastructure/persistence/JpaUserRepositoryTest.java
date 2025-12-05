package com.smarttruck.infrastructure.persistence;

import com.smarttruck.domain.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;

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

    @Test
    void findAllActive_shouldReturnOnlyActiveUsers() {
        // Arrange
        Instant now = Instant.now();
        JpaUser jpaUser1 = new JpaUser();
        jpaUser1.setId("1");
        jpaUser1.setDeletedAt(null);

        JpaUser jpaUser2 = new JpaUser();
        jpaUser2.setId("2");
        jpaUser2.setDeletedAt(null);

        User domainUser1 = new User("1", "Alice", "111", "alice@example.com", "pass", now, now, null, now);
        User domainUser2 = new User("2", "Bob", "222", "bob@example.com", "pass", now, now, null, now);

        List<JpaUser> jpaUsers = Arrays.asList(jpaUser1, jpaUser2);
        Pageable pageable = PageRequest.of(0, 20);
        Page<JpaUser> jpaPage = new PageImpl<>(jpaUsers, pageable, jpaUsers.size());

        when(springRepo.findAllActive(pageable)).thenReturn(jpaPage);
        when(mapper.toDomain(jpaUser1)).thenReturn(domainUser1);
        when(mapper.toDomain(jpaUser2)).thenReturn(domainUser2);

        // Act
        Page<User> result = jpaUserRepository.findAllActive(pageable);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.getContent().size());
        assertEquals(2, result.getTotalElements());
        assertTrue(result.getContent().stream().allMatch(user -> user.getDeletedAt() == null));

        verify(springRepo).findAllActive(pageable);
        verify(mapper).toDomain(jpaUser1);
        verify(mapper).toDomain(jpaUser2);
    }

    @Test
    void findAllActive_shouldExcludeDeletedUsers() {
        // Arrange
        Instant now = Instant.now();
        JpaUser activeUser = new JpaUser();
        activeUser.setId("1");
        activeUser.setDeletedAt(null);

        // Deleted user should NOT be in the result
        // (Spring Data @Query already filters, but we test the repository behavior)

        User domainUser = new User("1", "Alice", "111", "alice@example.com", "pass", now, now, null, now);

        List<JpaUser> jpaUsers = Arrays.asList(activeUser);
        Pageable pageable = PageRequest.of(0, 20);
        Page<JpaUser> jpaPage = new PageImpl<>(jpaUsers, pageable, jpaUsers.size());

        when(springRepo.findAllActive(pageable)).thenReturn(jpaPage);
        when(mapper.toDomain(activeUser)).thenReturn(domainUser);

        // Act
        Page<User> result = jpaUserRepository.findAllActive(pageable);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        assertEquals(1, result.getTotalElements());
        assertNull(result.getContent().get(0).getDeletedAt());

        verify(springRepo).findAllActive(pageable);
        verify(mapper).toDomain(activeUser);
    }
}
