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
    int age,
    String condition,
    LocalDate lastVisitDate
)
{}
