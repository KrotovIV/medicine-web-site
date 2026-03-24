package com.github.KrotovIV.backend.services;

import com.github.KrotovIV.backend.dto.PatientCardDtoResponse;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

@Service
@RequiredArgsConstructor
public class DataBaseService {

    private final MediaFileService mediaFileService;

    private Map<String, List<Long>> userToHisPatientsIds = new ConcurrentHashMap<>();
    private Map<Long, PatientCardDtoResponse> patientIdToPatientCard = new ConcurrentHashMap<>();

    private AtomicLong currentPatientId = new AtomicLong(0L);

    /**
     * Хардкод данных для теста
     */
    @PostConstruct
    public void init() {
        // добавляем пациентов
        patientIdToPatientCard.put(1L, PatientCardDtoResponse.builder()
                .id(1L)
                .avatar("👴")
                .name("Иванов Иван")
                .birthDate(LocalDate.of(1954, Month.FEBRUARY, 15))
                .condition("Гипертония, артрит")
                .lastVisitDate(LocalDate.now().minusDays(3))
                .build());

        patientIdToPatientCard.put(2L, PatientCardDtoResponse.builder()
                .id(2L)
                .avatar("\uD83D\uDC75")
                .name("Петрова Мария")
                .birthDate(LocalDate.of(1975, Month.MARCH, 10))
                .condition("Сахарный диабет 2 типа")
                .lastVisitDate(LocalDate.now().minusDays(1))
                .build());

        patientIdToPatientCard.put(3L, PatientCardDtoResponse.builder()
                .id(3L)
                .avatar("\uD83D\uDC68")
                .name("Сидоров Алексей")
                .birthDate(LocalDate.of(2000, Month.NOVEMBER, 21))
                .condition("Профилактический осмотр")
                .lastVisitDate(LocalDate.now().minusWeeks(2))
                .build());

        patientIdToPatientCard.put(4L, PatientCardDtoResponse.builder()
                .id(4L)
                .avatar("\uD83D\uDC69")
                .name("Козлова Елена")
                .birthDate(LocalDate.of(1999, Month.AUGUST, 1))
                .condition("Наблюдение, здоров")
                .lastVisitDate(LocalDate.now().minusMonths(1))
                .build());

        patientIdToPatientCard.put(5L, PatientCardDtoResponse.builder()
                .id(5L)
                .avatar("\uD83D\uDC74")
                .name("Николаев Петр")
                .birthDate(LocalDate.of(1995, Month.JULY, 11))
                .condition("ИБС, ХСН")
                .lastVisitDate(LocalDate.now().minusDays(5))
                .build());

        patientIdToPatientCard.put(6L, PatientCardDtoResponse.builder()
                .id(6L)
                .avatar("\uD83D\uDC75")
                .name("Смирнова Анна")
                .birthDate(LocalDate.of(1980, Month.OCTOBER, 10))
                .condition("Артроз, остеопороз")
                .lastVisitDate(LocalDate.now())
                .build());



        var list_ = new ArrayList<Long>();
        list_.addAll(List.of(1L, 2L, 3L, 4L, 5L, 6L));

        //добавляем этих пациентов в список пользователя test
        userToHisPatientsIds.put("test", list_);

        currentPatientId.set(7L);
    }

    public void registerUser(String userLogin) {
        if (!userToHisPatientsIds.containsKey(userLogin))
            userToHisPatientsIds.put(userLogin, new ArrayList<Long>());
    }

    public ResponseEntity<?> addPatient(String name, String emoji, LocalDate birthDate, String userLogin) {
        var id = currentPatientId.getAndIncrement();

        var patientCard = PatientCardDtoResponse.builder()
                .id(id)
                .avatar(emoji)
                .birthDate(birthDate)
                .name(name)
                .condition("{PATIENT_CONDITION}")
                .lastVisitDate(LocalDate.now())
                .build();

        // регистрация пациента
        patientIdToPatientCard.put(id, patientCard);

        // добавление пациента к пользователю
        var patientsIds = userToHisPatientsIds.get(userLogin).add(id);

        return ResponseEntity.ok().build();
    }

    public List<PatientCardDtoResponse> getPatients(String userLogin) {

        if (userToHisPatientsIds.containsKey(userLogin))
            return userToHisPatientsIds.get(userLogin).stream().map(
                    id -> patientIdToPatientCard.get(id)
            ).toList();

        return List.of();
    }

    public Optional<PatientCardDtoResponse> getPatientById(Long id) {
        return Optional.of(patientIdToPatientCard.get(id));
    }
}