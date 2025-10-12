package com.smarttruck.infra.repository;

import com.smarttruck.infra.entity.TicketEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaTicketRepository extends JpaRepository<TicketEntity, String> {

}
