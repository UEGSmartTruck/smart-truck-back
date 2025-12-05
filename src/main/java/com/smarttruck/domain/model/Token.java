package com.smarttruck.domain.model;

public class Token {
    private final String accessToken;
    private final String refreshToken;

    public Token(final String accessToken, final String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }
}
