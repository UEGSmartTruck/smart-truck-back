package com.smarttruck.application.usecase;

import com.smarttruck.domain.model.Token;

public interface RefreshTokenUseCase {
    Token refresh(String refreshToken) throws Exception;
}
