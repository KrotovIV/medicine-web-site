package com.github.KrotovIV.frontend.controllers;

import com.github.KrotovIV.frontend.baseLogging.LoggingDecorator;
import com.github.KrotovIV.frontend.dto.JwtDto;
import com.github.KrotovIV.frontend.dto.PatientCardDtoResponse;
import com.github.KrotovIV.frontend.services.LoginService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Controller
@RequestMapping("/login")
@RequiredArgsConstructor
public class LoginPageController {
    @Autowired
    WebClient webClient;

    @Autowired
    LoginService loginService;

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
        try {
            var result = loginService.login(username, password, response);

            // удачный вход
            if (result.getStatusCode().is2xxSuccessful())
                return "redirect:/";

            // неудачный вход
            return "redirect:/login?error=true";
        } catch (Exception e) {
            return "redirect:/login?error=true";
        }
    }

}
