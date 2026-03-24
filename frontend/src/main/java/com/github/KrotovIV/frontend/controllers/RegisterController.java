package com.github.KrotovIV.frontend.controllers;

import com.github.KrotovIV.frontend.baseLogging.LoggingDecorator;
import com.github.KrotovIV.frontend.dto.JwtDto;
import com.github.KrotovIV.frontend.services.LoginService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.reactive.function.client.WebClient;

@Controller
@RequestMapping("/register")
@RequiredArgsConstructor
public class RegisterController {
    @Autowired
    WebClient webClient;

    @Autowired
    LoginService loginService;

    private final String registerUrl = "http://127.0.0.1:8081/api/register";
    private final String authUrl = "http://127.0.0.1:8080/login"; // локальный эндпоинт на фронтенде

    @LoggingDecorator
    @PostMapping
    public String register(
            @RequestParam("username") String username,
            @RequestParam("password") String password,
            HttpServletResponse response
    ) {
        // регистрация
        var backendResponse = webClient.get()
                .uri(registerUrl, uriBuilder -> uriBuilder
                        .queryParam("login", username)
                        .queryParam("password", password)
                        .build()
                ).retrieve()
                .toEntity(JwtDto.class)
                .block();

        // вход
        var result = loginService.login(username, password, response);

        // удачный вход
        if (result.getStatusCode().is2xxSuccessful())
            return "redirect:/";

        // неудачный вход
        return "redirect:/login?error=true";
    }
}
