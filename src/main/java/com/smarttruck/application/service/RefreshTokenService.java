package com.smarttruck.application.service;

import com.smarttruck.application.usecase.RefreshTokenUseCase;
import com.smarttruck.domain.model.Token;
import com.smarttruck.domain.repository.RefreshTokenRepository;
import com.smarttruck.shared.security.JwtTokenProvider;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class RefreshTokenService implements RefreshTokenUseCase {

    private final RefreshTokenRepository repository;
    private final JwtTokenProvider jwtTokenProvider;

    public RefreshTokenService(final RefreshTokenRepository repository,
                               final JwtTokenProvider jwtTokenProvider) {
        this.repository = repository;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public Token refresh(final String refreshToken) throws Exception {
        final String userId = repository.findUserIdByRefreshToken(refreshToken);
        if (userId == null) {
            throw new Exception("Refresh token not found");
        }

        // For simplicity, create a new access token using userId as subject and email/name blank
        final String accessToken = jwtTokenProvider.generateToken(userId, "", "");

        // Issue a new refresh token and replace
        final String newRefresh = UUID.randomUUID().toString();
        repository.remove(refreshToken);
        repository.save(newRefresh, userId);

        return new Token(accessToken, newRefresh);
    }
}
