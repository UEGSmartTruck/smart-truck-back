package com.smarttruck.application.usecase;

import com.smarttruck.domain.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Use case para listar usuários ativos com paginação.
 */
public interface ListAllUserUseCase {
    /**
     * Lista usuários ativos (deletedAt IS NULL) com paginação.
     *
     * @param pageable parâmetros de paginação (page, size, sort)
     * @return página de usuários ativos
     */
    Page<User> execute(Pageable pageable);
}
