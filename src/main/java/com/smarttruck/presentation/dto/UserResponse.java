package com.smarttruck.presentation.dto;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;

// Usando 'record' para um DTO imutável e conciso (requer Java 14+).
// Se estiver em Java antigo, crie uma classe POJO normal com getters.
public record UserResponse(
    String id,
    String name,
    String email,
    String phone,
    Instant createdAt,
    Instant updatedAt
) {
    // Nota: O campo 'password' (hash) NÃO está incluído de propósito
}
