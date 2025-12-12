package com.smarttruck.application.usecase;

import com.smarttruck.domain.model.Client;

public interface GetClientByIdUseCase {

    Client execute(String id);
}
