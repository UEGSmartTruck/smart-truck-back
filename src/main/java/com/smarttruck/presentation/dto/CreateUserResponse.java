package com.smarttruck.presentation.dto;

import java.time.Instant;

public record CreateUserResponse(String id, String name, String email, String phone,
                                 Instant createdAt, Instant updatedAt, Instant deletedAt,
                                 Instant loginAt) {
}
