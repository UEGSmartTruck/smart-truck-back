package com.smarttruck.infra.repository;

import com.smarttruck.infra.entity.ClienteEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataClienteRepository extends JpaRepository<ClienteEntity, Long> {
}

