package com.smarttruck.presentation.dto;

public record ClientResponse(
    String id,
    String name,
    String email,
    String phone
) {
}
