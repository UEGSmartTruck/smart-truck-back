package com.smarttruck.application.service;

import com.smarttruck.application.usecase.FindAllUsersUseCase;
import com.smarttruck.domain.repository.UserRepository;
import com.smarttruck.presentation.dto.UserResponse;
import com.smarttruck.presentation.mapper.UserMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FindAllUsersService implements FindAllUsersUseCase {

    private final UserRepository userRepository;

    public FindAllUsersService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<UserResponse> execute() {
        // Busca todos os usuários do repositório
        var users = userRepository.findAll();

        // Converte a lista de entidades para a lista de DTOs usando o Mapper
        return UserMapper.toResponseList(users);
    }
}
