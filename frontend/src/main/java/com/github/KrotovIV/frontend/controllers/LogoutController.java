package com.github.KrotovIV.frontend.controllers;

import com.github.KrotovIV.frontend.baseLogging.LoggingDecorator;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/logout")
public class LogoutController {
    @LoggingDecorator
    @GetMapping
    public String logout(
            HttpServletResponse response
    ) {
        Cookie jwtCookie = new Cookie("jwtToken", null);
        jwtCookie.setPath("/");
        jwtCookie.setMaxAge(0);
        response.addCookie(jwtCookie);

        return "redirect:/";
    }
}
