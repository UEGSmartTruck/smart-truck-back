package com.smarttruck.presentation.dto;

public record CreateUserRequest(
    String name,
    String email,
    String password,
    String phone
) {}
