package com.smarttruck.infrastructure.persistence;

import com.smarttruck.domain.model.Client;
import org.springframework.stereotype.Component;

@Component
public class JpaClientMapper {
    public Client toDomain(JpaClient c) {
        if (c == null) return null;
        return new Client(c.getName(), c.getEmail(), c.getPhone());
    }
        public JpaClient toEntity(Client l) {
            if (l == null) return null;
            JpaClient c = new JpaClient();
            c.setId(l.getId());
            c.setName(l.getName());
            c.setEmail(l.getEmail());
            c.setPhone(l.getPhone());
            return c;
        }
    }
