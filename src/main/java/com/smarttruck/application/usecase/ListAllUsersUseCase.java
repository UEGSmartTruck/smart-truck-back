package com.smarttruck.application.usecase;
import com.smarttruck.domain.model.User;
import java.util.List;

public interface ListAllUsersUseCase {
    List<User> execute();
}
