package com.github.KrotovIV.frontend.models;

import lombok.Builder;

@Builder
public record PatientCard (
    String avatar,
    String name,
    String age,
    String condition,
    String lastVisitDate
)
{}
