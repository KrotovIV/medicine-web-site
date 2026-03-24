package com.github.KrotovIV.backend.services;

import com.github.KrotovIV.backend.dto.JwtDto;
import jakarta.annotation.PostConstruct;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class JwtTokenService {
    Map<String, String> usersToPasswords = new ConcurrentHashMap<>();
    Map<String, String> jwtAccessToUser = new ConcurrentHashMap<>();
    Map<String, String> jwtRefreshToAccess = new ConcurrentHashMap<>();

    public ResponseEntity<?> login(String login, String password) {
        // проверка что пользователь зарегистрирован
        if (!usersToPasswords.containsKey(login)) {
            return ResponseEntity.notFound().build();
        }

        // проверка что пароль правильный
        if (!usersToPasswords.get(login).equals(password)) {
            return ResponseEntity.status(401).body("Wrong Password");
        }

        // генерация токенов JWT
        var jwtAccess = generateString(20);
        var jwtRefresh = generateString(20);

        jwtAccessToUser.put(jwtAccess, login);
        jwtRefreshToAccess.put(jwtRefresh, jwtAccess);

        var response = JwtDto.builder()
                .access(jwtAccess)
                .refresh(jwtRefresh)
                .build();

        return ResponseEntity.ok(response);
    }

    public ResponseEntity<?> register(String login, String password) {
        if (usersToPasswords.containsKey(login))
            return ResponseEntity.status(409).body("Login already exists");

        // сохранение данных пользователя
        usersToPasswords.put(login, password);

        return ResponseEntity.ok().build();
    }

    /**
     * Хардкод тестового пользовтеля
     */
    @PostConstruct
    public void init() {
        String login = "test";
        String password = "password";
        usersToPasswords.put(login, password);
    }

    public boolean validateAccessToken(String jwtAccess) {
        return jwtAccessToUser.containsKey(jwtAccess);
    }

    public String getUserByAccessToken(String jwtAccess) {
        return jwtAccessToUser.get(jwtAccess);
    }

    private String generateString(int length) {
        var result = new StringBuilder();

        var symbols = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        var digits = "0123456789";

        var random = new Random();

        for (int i = 0; i < length; i++) {
            char symbol;

            int randCharId = random.nextInt();
            if (randCharId < 0)
                randCharId *= -1;

            if (random.nextBoolean()) {
                symbol = symbols.charAt(randCharId % symbols.length());
            } else {
                symbol = digits.charAt(randCharId % digits.length());;
            }

            if (random.nextBoolean()) {
                symbol = Character.toLowerCase(symbol);
            }

            result.append(symbol);

        }

        return result.toString();
    }
}
