package com.smarttruck.application.usecase;


import com.smarttruck.domain.model.User;


public interface CreateUserUseCase {
    User execute(String name, String email, String password, String phone);
}

