package com.smarttruck.application.service;

import com.smarttruck.application.usecase.ListAllUserUseCase;
import com.smarttruck.domain.model.User;
import com.smarttruck.domain.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * Serviço para listar usuários ativos com paginação.
 */
@Service
public class ListAllUserService implements ListAllUserUseCase {

    private final UserRepository userRepository;

    public ListAllUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Lista usuários ativos (deletedAt IS NULL) com paginação.
     *
     * @param pageable parâmetros de paginação
     * @return página de usuários ativos
     */
    @Override
    public Page<User> execute(Pageable pageable) {
        return userRepository.findAllActive(pageable);
    }
}
