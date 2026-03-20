package com.github.KrotovIV.frontend.controllers;

import com.github.KrotovIV.frontend.baseLogging.LoggingDecorator;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/add")
public class AddPatientCardController {
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
}
