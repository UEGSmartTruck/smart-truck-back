package com.smarttruck.presentation.dto;

import java.time.Instant;

/**
 * DTO padronizado para respostas de erro.
 * Usado por @RestControllerAdvice para retornar erros consistentes.
 */
public record ErrorResponse(
    String message,
    Instant timestamp,
    ErrorDetails details
) {
    /**
     * Detalhes adicionais do erro (código, campo, razão).
     */
    public record ErrorDetails(
        String code,
        String reason
    ) {
    }
}
