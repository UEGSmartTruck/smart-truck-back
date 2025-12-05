package com.smarttruck.presentation.dto;

/**
 * DTO para metadata de paginação.
 * Contém informações sobre a página atual e total de elementos conforme FR-010.
 */
public record PaginationMetadata(
    long totalElements,  // Total de usuários que atendem o filtro
    int totalPages,      // Total de páginas com o pageSize atual
    int currentPage,     // Página atual (zero-indexed)
    int pageSize         // Itens por página
) {
}
