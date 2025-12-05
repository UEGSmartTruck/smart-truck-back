package com.smarttruck.presentation.dto;

import java.time.Instant;

/**
 * DTO representando dados de um usuário na listagem.
 * Espelha os 8 campos obrigatórios do domain User conforme FR-002.
 */
public record UserData(
    String id,
    String name,
    String email,
    String phone,        // Nullable
    Instant createdAt,
    Instant updatedAt,   // Nullable
    Instant deletedAt,   // Sempre null para usuários ativos
    Instant loginAt      // Nullable
) {
}
