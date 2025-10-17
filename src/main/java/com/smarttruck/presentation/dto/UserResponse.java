package com.smarttruck.presentation.dto;

import java.time.Instant;

// Usando 'record' para um DTO conciso e imutável.
public record UserResponse(
    String id,
    String name,
    String email,
    String phone,
    Instant createdAt
) {
    // Não precisamos de um metodo 'fromEntity' aqui,
    // pois usaremos um Mapper dedicado para manter a consistência.
}
