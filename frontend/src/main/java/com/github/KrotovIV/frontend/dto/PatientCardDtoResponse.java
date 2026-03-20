package com.github.KrotovIV.frontend.dto;

import lombok.Builder;

import java.time.LocalDate;

/**
 * Данные карточки пациента, полученные с бекенда
 */
@Builder
public record PatientCardDtoResponse(
    String avatar,
    String name,
    int age,
    String condition,
    LocalDate lastVisitDate
)
{}
