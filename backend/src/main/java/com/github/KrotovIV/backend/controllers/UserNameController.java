package com.github.KrotovIV.backend.controllers;

import com.github.KrotovIV.backend.baseLogging.LoggingDecorator;
import com.github.KrotovIV.backend.services.JwtTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Временный хардкод JWT
 */
@RestController
@RequestMapping("/api/username")
@RequiredArgsConstructor
public class UserNameController {
    @Autowired
    JwtTokenService jwtTokenService;

    @LoggingDecorator
    @GetMapping
    public ResponseEntity<?> getJwt(
            @CookieValue("jwtToken") String jwtToken
    ) {
        var result = jwtTokenService.getUserByAccessToken(jwtToken);
        return ResponseEntity.ok(result);
    }
}
