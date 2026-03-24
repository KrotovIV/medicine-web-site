package com.github.KrotovIV.backend.dto;

import lombok.Builder;

@Builder
public record JwtDto (
    String access,
    String refresh
)
{}
