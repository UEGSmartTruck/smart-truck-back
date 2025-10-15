package com.smarttruck.presentation.dto;

import lombok.Data;

// Esta classe representa os dados do usuário que serão enviados de volta
// como resposta para o frontend. NUNCA inclua a senha aqui.
@Data
public class UserResponse {
    private Long id;
    private String email;
}
