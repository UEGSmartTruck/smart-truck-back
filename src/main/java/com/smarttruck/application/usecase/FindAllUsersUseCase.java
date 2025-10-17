package com.smarttruck.application.usecase;

import com.smarttruck.presentation.dto.UserResponse;
import java.util.List;

public interface FindAllUsersUseCase {
    List<UserResponse> execute();
}
