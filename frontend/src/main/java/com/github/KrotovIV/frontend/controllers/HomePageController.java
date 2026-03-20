package com.github.KrotovIV.frontend.controllers;

import com.github.KrotovIV.frontend.baseLogging.LoggingDecorator;
import com.github.KrotovIV.frontend.models.PatientCard;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Controller
@RequestMapping("/")
@RequiredArgsConstructor
public class HomePageController {
    private final WebClient webClient;

    private final String getPatientsListUrl = "http://127.0.0.1:8081/api/patients/list";

    @LoggingDecorator
    @GetMapping("/")
    public String home(@CookieValue(value="jwtToken", required = false) String jwtToken, Model model) {
        // проверка наличия jwt-токена в куках
        boolean isAuthenticated = jwtToken != null && !jwtToken.isEmpty();

        if (!isAuthenticated) {
            return "redirect:/login";
        }

        // отображение статуса пользователя на странице
        model.addAttribute("isAuthenticated", isAuthenticated);
        model.addAttribute("username", "Пользователь");

//        // временно хардкод списка пациентов
//        var patientsList = List.of(
//            PatientCard.builder()
//                    .avatar("👴")
//                    .name("Иванов Иван")
//                    .age("72 года")
//                    .condition("Гипертония, артрит")
//                    .lastVisitDate("\uD83E\uDE7A Последний визит: 3 дня назад")
//                    .build(),
//
//            PatientCard.builder()
//                    .avatar("\uD83D\uDC75")
//                    .name("Петрова Мария")
//                    .age("68 лет")
//                    .condition("Сахарный диабет 2 типа")
//                    .lastVisitDate("\uD83E\uDE7A Последний визит: Вчера")
//                    .build(),
//
//            PatientCard.builder()
//                    .avatar("\uD83D\uDC68")
//                    .name("Сидоров Алексей")
//                    .age("45 лет")
//                    .condition("Профилактический осмотр")
//                    .lastVisitDate("\uD83E\uDE7A Последний визит: 2 недели назад")
//                    .build(),
//
//            PatientCard.builder()
//                    .avatar("\uD83D\uDC69")
//                    .name("Козлова Елена")
//                    .age("34 года")
//                    .condition("Наблюдение, здоров")
//                    .lastVisitDate("\uD83E\uDE7A Последний визит: 1 месяц назад")
//                    .build(),
//
//            PatientCard.builder()
//                    .avatar("\uD83D\uDC74")
//                    .name("Николаев Петр")
//                    .age("81 год")
//                    .condition("ИБС, ХСН")
//                    .lastVisitDate("\uD83E\uDE7A Последний визит: 5 дней назад")
//                    .build(),
//
//
//            PatientCard.builder()
//                    .avatar("\uD83D\uDC75")
//                    .name("Смирнова Анна")
//                    .age("57 лет")
//                    .condition("Артроз, остеопороз")
//                    .lastVisitDate("\uD83E\uDE7A Последний визит: Сегодня утром")
//                    .build()
//
//        );


        var patientsList = webClient.get()
                        .uri(getPatientsListUrl)
                        .retrieve()
                        .bodyToFlux(PatientCard.class)
                        .collectList()
                        .block();


        model.addAttribute("patients", patientsList);


        return "home_page";
    }
}
