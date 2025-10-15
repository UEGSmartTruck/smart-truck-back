package com.smarttruck.presentation.mapper;

import com.smarttruck.domain.model.User;
import com.smarttruck.presentation.dto.UserResponse;
import org.springframework.stereotype.Component;

// Esta classe é responsável por "mapear" ou "converter" um objeto
// do nosso domínio (User) para um objeto de transferência de dados (UserResponse).
@Component
public class UserMapper {

    public UserResponse toResponse(User user) {
        // Cria um novo objeto de resposta
        UserResponse response = new UserResponse();

        // Pega os dados do usuário do domínio e coloca no objeto de resposta
        response.setId(user.getId());
        response.setEmail(user.getEmail());

        // Retorna o objeto de resposta preenchido
        return response;
    }
}
