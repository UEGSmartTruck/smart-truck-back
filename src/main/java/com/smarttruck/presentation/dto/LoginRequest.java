package com.smarttruck.presentation.dto;

import lombok.Data;

// Esta classe representa os dados que chegam do frontend para fazer o login.
@Data
public class LoginRequest {
    private String email;
    private String password;
}
