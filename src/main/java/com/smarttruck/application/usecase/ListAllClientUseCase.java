package com.smarttruck.application.usecase;

import com.smarttruck.domain.model.Client;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface ListAllClientUseCase {

    Page<Client> execute(Pageable pageable);

    Optional<Client> findById(String id);
}
