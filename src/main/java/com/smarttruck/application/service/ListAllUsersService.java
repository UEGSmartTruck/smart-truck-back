package com.smarttruck.application.service;

import com.smarttruck.application.usecase.ListAllUsersUseCase;
import com.smarttruck.domain.model.User;
import com.smarttruck.domain.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ListAllUsersService implements ListAllUsersUseCase {

    private final UserRepository userRepository;

    public ListAllUsersService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<User> execute() {
        return userRepository.findAll();
    }
}
