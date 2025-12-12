package com.smarttruck.application.usecase;

import com.smarttruck.domain.model.Client;

import java.util.UUID;

public interface UpdateClientUseCase {
    Client execute(UUID id, String name, String email, String phone);
}
