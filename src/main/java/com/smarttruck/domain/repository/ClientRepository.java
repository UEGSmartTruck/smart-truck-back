package com.smarttruck.domain.repository;

import com.smarttruck.domain.model.Client;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface ClientRepository {

    Client save(Client client);

    boolean existsByEmail(String email);
    Optional<Client> findByEmail(String email);
    Optional<Client> findById(String id);
    List<Client> findAll();
    void deleteById(String id);
    Page<Client> findAllPageable(Pageable pageable);

}
