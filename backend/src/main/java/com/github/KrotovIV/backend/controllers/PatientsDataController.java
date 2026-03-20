package com.github.KrotovIV.backend.controllers;

import com.github.KrotovIV.backend.baseLogging.LoggingDecorator;
import com.github.KrotovIV.backend.dto.PatientCardDtoResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
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
                        .age(72)
                        .condition("Гипертония, артрит")
                        .lastVisitDate(LocalDate.now().minusDays(3))
                        .build(),

                PatientCardDtoResponse.builder()
                        .avatar("\uD83D\uDC75")
                        .name("Петрова Мария")
                        .age(68)
                        .condition("Сахарный диабет 2 типа")
                        .lastVisitDate(LocalDate.now().minusDays(1))
                        .build(),

                PatientCardDtoResponse.builder()
                        .avatar("\uD83D\uDC68")
                        .name("Сидоров Алексей")
                        .age(45)
                        .condition("Профилактический осмотр")
                        .lastVisitDate(LocalDate.now().minusWeeks(2))
                        .build(),

                PatientCardDtoResponse.builder()
                        .avatar("\uD83D\uDC69")
                        .name("Козлова Елена")
                        .age(34)
                        .condition("Наблюдение, здоров")
                        .lastVisitDate(LocalDate.now().minusMonths(1))
                        .build(),

                PatientCardDtoResponse.builder()
                        .avatar("\uD83D\uDC74")
                        .name("Николаев Петр")
                        .age(81)
                        .condition("ИБС, ХСН")
                        .lastVisitDate(LocalDate.now().minusDays(5))
                        .build(),


                PatientCardDtoResponse.builder()
                        .avatar("\uD83D\uDC75")
                        .name("Смирнова Анна")
                        .age(57)
                        .condition("Артроз, остеопороз")
                        .lastVisitDate(LocalDate.now())
                        .build()

        );

        return ResponseEntity.ok(patientsList);
    }
}
