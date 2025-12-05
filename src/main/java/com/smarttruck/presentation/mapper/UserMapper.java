package com.smarttruck.presentation.mapper;


import com.smarttruck.domain.model.User;
import com.smarttruck.presentation.dto.CreateUserResponse;
import org.springframework.stereotype.Component;


@Component
public class UserMapper {

    private UserMapper() {
    }

    public static CreateUserResponse toResponse(final User user) {
        final CreateUserResponse r =
            new CreateUserResponse(user.getId(), user.getName(), user.getEmail(), user.getPhone(),
                user.getCreatedAt(), user.getUpdatedAt(), user.getDeletedAt(), user.getLoginAt());
        return r;
    }
}
