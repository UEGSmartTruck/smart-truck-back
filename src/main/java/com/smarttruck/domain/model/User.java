package com.smarttruck.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class User {
    private Long id;
    private String email;
    private String password; // Apenas para lógica de negócio, não será exposto
}
