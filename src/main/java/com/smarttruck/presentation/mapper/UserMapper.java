package com.smarttruck.presentation.mapper;


import com.smarttruck.domain.model.User;
import com.smarttruck.presentation.dto.CreateUserResponse;
import org.springframework.stereotype.Component;


@Component
public class UserMapper {

    private UserMapper() {
    }

    public static CreateUserResponse toResponse(User user) {
        CreateUserResponse r = new CreateUserResponse();
        r.setId(user.getId());
        r.setName(user.getName());
        r.setEmail(user.getEmail());
        r.setPhone(user.getPhone());
        r.setCreatedAt(user.getCreatedAt());
        r.setUpdatedAt(user.getUpdatedAt());
        r.setDeletedAt(user.getDeletedAt());
        r.setLoginAt(user.getLoginAt());
        return r;
    }
}
