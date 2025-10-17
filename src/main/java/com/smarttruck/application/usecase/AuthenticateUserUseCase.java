package com.smarttruck.application.usecase;

public interface AuthenticateUserUseCase {
    String execute(String email, String password);
}
