package com.github.KrotovIV.frontend.controllers;

import com.github.KrotovIV.frontend.baseLogging.LoggingDecorator;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/login")
public class LoginPageController {

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

        // временно заглушка
        // здесь должен быть вызов запроса к backend
        if (username.equals("test") && password.equals("password")) {
            System.out.println("Пользователь авторизован");

            // Создаём заглушку JWT токена
            String fakeJwt = "fake-jwt-token-for-" + username;

            // Сохраняем токен в cookie
            Cookie jwtCookie = new Cookie("jwtToken", fakeJwt);
            jwtCookie.setPath("/");
            jwtCookie.setMaxAge(10 * 60); // 10 минут
            response.addCookie(jwtCookie);

            return "redirect:/";
        }


        System.out.println("Пользователь НЕ авторизован");
        return "redirect:/login";
    }

}
