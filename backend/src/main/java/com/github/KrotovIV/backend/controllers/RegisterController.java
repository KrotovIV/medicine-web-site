package com.github.KrotovIV.backend.controllers;

import com.github.KrotovIV.backend.baseLogging.LoggingDecorator;
import com.github.KrotovIV.backend.services.DataBaseService;
import com.github.KrotovIV.backend.services.JwtTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/register")
@RequiredArgsConstructor
public class RegisterController {
    @Autowired
    JwtTokenService jwtTokenService;

    @Autowired
    DataBaseService dataBaseService;

    @LoggingDecorator
    @GetMapping
    public ResponseEntity<?> getJwt(
            @RequestParam("login") String login,
            @RequestParam("password") String password
    ) {
        dataBaseService.registerUser(login);
        return jwtTokenService.register(login, password);
    }
}
