package com.github.KrotovIV.frontend.controllers;

import com.github.KrotovIV.frontend.baseLogging.LoggingDecorator;
import com.github.KrotovIV.frontend.dto.JwtDto;
import com.github.KrotovIV.frontend.dto.PatientCardDtoResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.reactive.function.client.WebClient;

@Controller
@RequestMapping("/login")
@RequiredArgsConstructor
public class LoginPageController {
    @Autowired
    WebClient webClient;

    private final String loginUrl = "http://127.0.0.1:8081/api/login";

    @LoggingDecorator
    @GetMapping
    public String loginpage() {
        return "login"; // возвращаем темплейт login
    }

    @LoggingDecorator
    @PostMapping
    public String loginPost(
            @RequestParam("username") String username,
            @RequestParam("password") String password,
            HttpServletResponse response
    ) {

        var backendResponse = webClient.get()
                .uri(loginUrl, uriBuilder -> uriBuilder
                        .queryParam("login", username)
                        .queryParam("password", password)
                        .build()
                ).retrieve()
                .toEntity(JwtDto.class)
                .block();

        if (backendResponse.getStatusCode().is2xxSuccessful()) {
            // Успешный ответ - получаем JWT
            JwtDto jwtDto = backendResponse.getBody();

            // Создаём cookie с токеном
            Cookie jwtCookie = new Cookie("jwtToken", jwtDto.access());
            jwtCookie.setPath("/");
            jwtCookie.setMaxAge(10 * 60);
            response.addCookie(jwtCookie);

            return "redirect:/";
        } else {
            // Неуспешный ответ
            System.out.println("Пользователь НЕ авторизован. Статус: " +
                    backendResponse.getStatusCode());
            return "redirect:/login";
        }

    }

}
