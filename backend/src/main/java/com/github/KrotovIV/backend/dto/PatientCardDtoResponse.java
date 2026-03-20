package com.github.KrotovIV.backend.dto;

import lombok.Builder;
import java.time.LocalDate;

/**
 * Данные карточки пациента для отправки на фронт
 */
@Builder
public record PatientCardDtoResponse (
    String avatar,
    String name,
    LocalDate birthDate,
    String condition,
    LocalDate lastVisitDate
)
{}
