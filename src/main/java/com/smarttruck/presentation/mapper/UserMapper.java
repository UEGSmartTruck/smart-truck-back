package com.smarttruck.presentation.mapper;


import com.smarttruck.domain.model.User;
import com.smarttruck.presentation.dto.CreateUserResponse;
import com.smarttruck.presentation.dto.ListAllUserResponse;
import com.smarttruck.presentation.dto.PaginationMetadata;
import com.smarttruck.presentation.dto.UserData;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.List;


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

    /**
     * Converte um User de domínio para UserData DTO.
     *
     * @param user usuário do domínio
     * @return UserData DTO
     */
    public static UserData toUserData(User user) {
        return new UserData(
            user.getId(),
            user.getName(),
            user.getEmail(),
            user.getPhone(),
            user.getCreatedAt(),
            user.getUpdatedAt(),
            user.getDeletedAt(),
            user.getLoginAt()
        );
    }

    /**
     * Converte uma Page de Users para ListAllUserResponse.
     *
     * @param page página de usuários do domínio
     * @return ListAllUserResponse com dados e metadados de paginação
     */
    public static ListAllUserResponse toListAllResponse(Page<User> page) {
        List<UserData> users = page.getContent().stream()
            .map(UserMapper::toUserData)
            .toList();

        PaginationMetadata metadata = new PaginationMetadata(
            page.getTotalElements(),
            page.getTotalPages(),
            page.getNumber(),
            page.getSize()
        );

        return new ListAllUserResponse(users, metadata);
    }
}
