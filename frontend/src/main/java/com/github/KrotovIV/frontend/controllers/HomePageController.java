package com.github.KrotovIV.frontend.controllers;

import com.github.KrotovIV.frontend.baseLogging.LoggingDecorator;
import com.github.KrotovIV.frontend.dto.PatientCardDtoResponse;
import com.github.KrotovIV.frontend.formatters.PatientCardFormatter;
import com.github.KrotovIV.frontend.models.PatientCard;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private final PatientCardFormatter patientCardFormatter;

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

        // получение списка пациентов с бекенда
        var patientsList = webClient.get()
                        .uri(getPatientsListUrl)
                        .retrieve()
                        .bodyToFlux(PatientCardDtoResponse.class)
                        .collectList()
                        .block();

        // форматирование данных
        var formattedPatientsList = patientsList.stream().map(
                card -> PatientCard.builder()
                        .avatar(card.avatar())
                        .name(card.name())
                        .age(patientCardFormatter.formatAge(card.birthDate()))
                        .condition(card.condition())
                        .lastVisitDate(patientCardFormatter.formatRelativeDate(card.lastVisitDate()))
                        .build()
        ).toList();

        model.addAttribute("patients", formattedPatientsList);


        return "home_page";
    }
}
