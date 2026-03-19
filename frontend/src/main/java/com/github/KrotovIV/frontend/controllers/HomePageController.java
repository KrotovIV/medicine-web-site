package com.github.KrotovIV.frontend.controllers;

import com.github.KrotovIV.frontend.baseLogging.LoggingDecorator;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class HomePageController {
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


        return "home_page";
    }
}
