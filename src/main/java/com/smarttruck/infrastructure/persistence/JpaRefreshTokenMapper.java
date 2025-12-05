package com.smarttruck.infrastructure.persistence;

import org.springframework.stereotype.Component;

@Component
public class JpaRefreshTokenMapper {

    public JpaRefreshToken toEntity(final String token, final String userId) {
        if (token == null || userId == null) return null;
        return new JpaRefreshToken(token, userId);
    }
}
