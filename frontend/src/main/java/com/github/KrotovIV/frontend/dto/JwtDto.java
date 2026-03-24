package com.github.KrotovIV.frontend.dto;

import lombok.Builder;

@Builder
public record JwtDto(
    String access,
    String refresh
)
{}
