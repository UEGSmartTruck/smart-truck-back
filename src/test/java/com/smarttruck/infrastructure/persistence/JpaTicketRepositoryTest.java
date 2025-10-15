package com.smarttruck.infrastructure.persistence;

import com.smarttruck.domain.model.Ticket;
import com.smarttruck.domain.model.TicketStatus;
import com.smarttruck.domain.repository.TicketRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class JpaTicketRepositoryTest {

    @Mock
    private SpringDataJpaTicketRepository springDataRepository;

    @Mock
    private JpaTicketMapper mapper;

    private TicketRepository jpaTicketRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        jpaTicketRepository = new JpaTicketRepository(springDataRepository, mapper);
    }

    @Test
    void shouldSaveTicketSuccessfully() {
        // Arrange
        String id = UUID.randomUUID().toString();
        Instant now = Instant.now();

        Ticket domainTicket =
            new Ticket(id, "C001", "Sensor issue", TicketStatus.OPEN, now, null, null);
        JpaTicket entity = new JpaTicket(id, "C001", "Sensor issue", "OPEN", now, null, null);
        JpaTicket savedEntity = new JpaTicket(id, "C001", "Sensor issue", "OPEN", now, now, null);
        Ticket savedDomain =
            new Ticket(id, "C001", "Sensor issue", TicketStatus.OPEN, now, now, null);

        when(mapper.toEntity(domainTicket)).thenReturn(entity);
        when(springDataRepository.save(entity)).thenReturn(savedEntity);
        when(mapper.toDomain(savedEntity)).thenReturn(savedDomain);

        // Act
        Ticket result = jpaTicketRepository.save(domainTicket);

        // Assert
        verify(mapper).toEntity(domainTicket);
        verify(springDataRepository).save(entity);
        verify(mapper).toDomain(savedEntity);
        assertEquals(savedDomain, result);
    }

    @Test
    void shouldPropagateExceptionFromRepository() {
        // Arrange
        Ticket domainTicket = new Ticket("C002", "Engine fault");
        JpaTicket entity =
            new JpaTicket("1", "C002", "Engine fault", "OPEN", Instant.now(), null, null);

        when(mapper.toEntity(domainTicket)).thenReturn(entity);
        when(springDataRepository.save(entity)).thenThrow(new RuntimeException("DB error"));

        // Act & Assert
        RuntimeException exception =
            assertThrows(RuntimeException.class, () -> jpaTicketRepository.save(domainTicket));
        assertEquals("DB error", exception.getMessage());

        verify(mapper).toEntity(domainTicket);
        verify(springDataRepository).save(entity);
        verify(mapper, never()).toDomain(any());
    }

    @Test
    void shouldHandleNullTicketGracefullyIfMapperReturnsNull() {
        // Arrange
        Ticket domainTicket = new Ticket("C003", "Null case");

        when(mapper.toEntity(domainTicket)).thenReturn(null);

        // Act & Assert
        assertThrows(NullPointerException.class, () -> jpaTicketRepository.save(domainTicket));

        verify(mapper).toEntity(domainTicket);
        verify(springDataRepository, never()).save(any());
    }
}
