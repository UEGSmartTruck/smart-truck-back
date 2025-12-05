package com.smarttruck.application.service;

import com.smarttruck.domain.repository.RefreshTokenRepository;
import com.smarttruck.shared.security.JwtTokenProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class RefreshTokenServiceTest {

    private RefreshTokenRepository repo;
    private JwtTokenProvider jwt;
    private RefreshTokenService service;

    @BeforeEach
    void setUp() {
        repo = mock(RefreshTokenRepository.class);
        jwt = mock(JwtTokenProvider.class);
        service = new RefreshTokenService(repo, jwt);
    }

    @Test
    void refresh_shouldReturnNewTokens_whenRefreshExists() throws Exception {
        final String old = "old-refresh";
        when(repo.findUserIdByRefreshToken(old)).thenReturn("user-1");
        when(jwt.generateToken(eq("user-1"), anyString(), anyString())).thenReturn("new-access");

        final var token = service.refresh(old);

        assertEquals("new-access", token.getAccessToken());
        assertNotNull(token.getRefreshToken());
        verify(repo).remove(old);
        verify(repo).save(anyString(), eq("user-1"));
    }

    @Test
    void refresh_shouldThrow_whenNotFound() {
        final String old = "not-exist";
        when(repo.findUserIdByRefreshToken(old)).thenReturn(null);

        assertThrows(Exception.class, () -> service.refresh(old));
    }
}
