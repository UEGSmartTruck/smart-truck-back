package com.smarttruck.application.usecase;

import com.smarttruck.domain.model.Client;

public interface CreateClientUseCase {

    Client execute(String name, String email, String phone);
}
