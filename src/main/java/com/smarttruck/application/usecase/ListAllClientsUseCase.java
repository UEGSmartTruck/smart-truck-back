package com.smarttruck.application.usecase;

import com.smarttruck.domain.model.Client;

import java.util.List;

public interface ListAllClientsUseCase {
    List<Client> execute();
}
