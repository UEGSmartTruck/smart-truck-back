package com.smarttruck.presentation.dto;

public record TokenResponse(String accessToken, long expiresIn) {
}
