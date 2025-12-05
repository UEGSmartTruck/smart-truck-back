package com.smarttruck.presentation.dto;

import java.util.List;

/**
 * DTO para resposta de listagem de usuários.
 * Encapsula lista de usuários com metadata de paginação.
 */
public record ListAllUserResponse(
    List<UserData> users,
    PaginationMetadata metadata
) {
}
