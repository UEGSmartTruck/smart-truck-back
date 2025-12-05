package com.smarttruck.presentation.mapper;

import com.smarttruck.domain.model.Token;
import com.smarttruck.presentation.dto.RefreshTokenResponse;

public class RefreshTokenMapper {
    public static RefreshTokenResponse toResponse(final Token token) {
        return new RefreshTokenResponse(token.getAccessToken(), token.getRefreshToken());
    }
}
