package com.github.KrotovIV.frontend.dto;

import lombok.Builder;

import java.time.LocalDate;

/**
 * Данные карточки пациента, полученные с бекенда
 */
@Builder
public record PatientCardDtoResponse(
    Long id,
    String avatar,
    String name,
    LocalDate birthDate,
    String condition,
    LocalDate lastVisitDate
)
{}
