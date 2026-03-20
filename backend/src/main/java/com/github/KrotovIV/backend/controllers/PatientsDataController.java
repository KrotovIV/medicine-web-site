package com.github.KrotovIV.backend.controllers;

import com.github.KrotovIV.backend.baseLogging.LoggingDecorator;
import com.github.KrotovIV.backend.dto.PatientCardDtoResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;

@RestController
@RequestMapping("/api/patients")
public class PatientsDataController {
    @LoggingDecorator
    @GetMapping("/list")
    public ResponseEntity<List<PatientCardDtoResponse>> getPatientsCardsList() {
        // временный хардкод
        var patientsList = List.of(
                PatientCardDtoResponse.builder()
                        .avatar("👴")
                        .name("Иванов Иван")
                        .birthDate(LocalDate.of(1954, Month.FEBRUARY, 15))
                        .condition("Гипертония, артрит")
                        .lastVisitDate(LocalDate.now().minusDays(3))
                        .build(),

                PatientCardDtoResponse.builder()
                        .avatar("\uD83D\uDC75")
                        .name("Петрова Мария")
                        .birthDate(LocalDate.of(1975, Month.MARCH, 10))
                        .condition("Сахарный диабет 2 типа")
                        .lastVisitDate(LocalDate.now().minusDays(1))
                        .build(),

                PatientCardDtoResponse.builder()
                        .avatar("\uD83D\uDC68")
                        .name("Сидоров Алексей")
                        .birthDate(LocalDate.of(2000, Month.NOVEMBER, 21))
                        .condition("Профилактический осмотр")
                        .lastVisitDate(LocalDate.now().minusWeeks(2))
                        .build(),

                PatientCardDtoResponse.builder()
                        .avatar("\uD83D\uDC69")
                        .name("Козлова Елена")
                        .birthDate(LocalDate.of(1999, Month.AUGUST, 1))
                        .condition("Наблюдение, здоров")
                        .lastVisitDate(LocalDate.now().minusMonths(1))
                        .build(),

                PatientCardDtoResponse.builder()
                        .avatar("\uD83D\uDC74")
                        .name("Николаев Петр")
                        .birthDate(LocalDate.of(1995, Month.JULY, 11))
                        .condition("ИБС, ХСН")
                        .lastVisitDate(LocalDate.now().minusDays(5))
                        .build(),


                PatientCardDtoResponse.builder()
                        .avatar("\uD83D\uDC75")
                        .name("Смирнова Анна")
                        .birthDate(LocalDate.of(1980, Month.OCTOBER, 10))
                        .condition("Артроз, остеопороз")
                        .lastVisitDate(LocalDate.now())
                        .build()

        );

        return ResponseEntity.ok(patientsList);
    }
}
