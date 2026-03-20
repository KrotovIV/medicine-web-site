package com.github.KrotovIV.frontend.models;

import lombok.Builder;

/**
 * Данные пациента для отображения в карточке в общем списке
 */
@Builder
public record PatientCard (
    String avatar,
    String name,
    String age,
    String condition,
    String lastVisitDate
)
{}
