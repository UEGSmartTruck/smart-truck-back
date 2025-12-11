package com.smarttruck.application.usecase;

import com.smarttruck.domain.model.User;
import java.util.UUID;

public interface UpdateUserUseCase {
    User execute(UUID id, String name, String email, String phone);
}
