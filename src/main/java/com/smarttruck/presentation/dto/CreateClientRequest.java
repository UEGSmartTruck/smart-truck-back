package com.smarttruck.presentation.dto;

public record CreateClientRequest(
    String name,
    String email,
    String phone
) {}
