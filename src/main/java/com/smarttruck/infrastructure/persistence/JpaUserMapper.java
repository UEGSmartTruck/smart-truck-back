package com.smarttruck.infrastructure.persistence;


import com.smarttruck.domain.model.User;
import org.springframework.stereotype.Component;


@Component
public class JpaUserMapper {
    public User toDomain(JpaUser e) {
        if (e == null) return null;
        return new User(e.getId(), e.getName(), e.getPhone(), e.getEmail(), e.getPasswordHash(),
            e.getCreatedAt(), e.getUpdatedAt(), e.getDeletedAt(), e.getLoginAt());
    }


    public JpaUser toEntity(User u) {
        if (u == null) return null;
        JpaUser e = new JpaUser();
        e.setId(u.getId());
        e.setName(u.getName());
        e.setPhone(u.getPhone());
        e.setEmail(u.getEmail());
        e.setPasswordHash(u.getPasswordHash());
        e.setCreatedAt(u.getCreatedAt());
        e.setUpdatedAt(u.getUpdatedAt());
        e.setDeletedAt(u.getDeletedAt());
        e.setLoginAt(u.getLoginAt());
        return e;
    }
}
