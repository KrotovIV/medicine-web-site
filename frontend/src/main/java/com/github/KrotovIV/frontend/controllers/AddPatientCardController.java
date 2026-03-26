package com.github.KrotovIV.frontend.controllers;

import com.github.KrotovIV.frontend.baseLogging.LoggingDecorator;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

@Controller
@RequestMapping("/add")
@RequiredArgsConstructor
public class AddPatientCardController {
    @Autowired
    WebClient webClient;

    private final String addPatientUrl = "http://127.0.0.1:8081/api/patients/patient/add";

    @LoggingDecorator
    @GetMapping
    public String addPatientCardView(@CookieValue(value="jwtToken", required = false) String jwtToken) {
        // проверка наличия jwt-токена в куках
        boolean isAuthenticated = jwtToken != null && !jwtToken.isEmpty();

        if (!isAuthenticated) {
            return "redirect:/login";
        }

        return "add_patient_card";
    }

    @LoggingDecorator
    @PostMapping
    public String addPatientCardPost(
            @CookieValue(value="jwtToken", required = false) String jwtToken,
            @RequestParam("avatarEmoji") String avatarEmoji,
            @RequestParam("name") String name,
            @RequestParam("birthDate") LocalDate birthDate,
            @RequestParam("condition") String condition,
            HttpServletResponse response
    ) {

        boolean isAuthenticated = jwtToken != null && !jwtToken.isEmpty();

        // запрос на добавление клиента к бекенду
        var backendResponse = webClient.post()
                .uri(addPatientUrl, uriBuilder -> uriBuilder
                        .queryParam("name", name)
                        .queryParam("birthDate", birthDate)
                        .queryParam("emoji", avatarEmoji)
                        .queryParam("condition", condition)
                        .build())
                .cookie("jwtToken", jwtToken)
                .retrieve()
                .onStatus(HttpStatusCode::isError, clientResponse -> {
                    System.out.println("Ошибка при добавлении клиента");
                    return Mono.empty();
                })
                .bodyToMono(String.class)
                .block();

        // возвращаемся на главную страницу
        return "redirect:/";
    }
}
