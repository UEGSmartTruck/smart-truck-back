package com.smarttruck.presentation.dto;

import java.util.List;

public record ListAllClientResponse(List<ClientData> clients,
                                    PaginationMetadata metadata
){}
