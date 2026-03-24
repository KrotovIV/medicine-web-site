package com.github.KrotovIV.frontend.services;

import com.github.KrotovIV.frontend.dto.JwtDto;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class LoginService {

    private final String loginUrl = "http://127.0.0.1:8081/api/login";

    @Autowired
    private WebClient webClient;

    public ResponseEntity<?> login(
            String username,
            String password,
            HttpServletResponse response
    ) {
        try {
            ResponseEntity<JwtDto> backendResponse = webClient.get()
                    .uri(loginUrl, uriBuilder -> uriBuilder
                            .queryParam("login", username)
                            .queryParam("password", password)
                            .build()
                    )
                    .retrieve()
                    .onStatus(
                            HttpStatusCode::isError,
                            clientResponse -> {
                                System.out.println("Authentication failed with status: " + clientResponse.statusCode());
                                return Mono.empty(); // Превращаем ошибку в пустой Mono
                            }
                    )
                    .toEntity(JwtDto.class)
                    .block();

            if (backendResponse != null && backendResponse.getStatusCode().is2xxSuccessful()) {
                JwtDto jwtDto = backendResponse.getBody();

                Cookie jwtCookie = new Cookie("jwtToken", jwtDto.access());
                jwtCookie.setPath("/");
                jwtCookie.setMaxAge(10 * 60);
                response.addCookie(jwtCookie);

//                return "redirect:/";
                return ResponseEntity.ok().build();
            } else {
                System.out.println("Пользователь НЕ авторизован");
//                return "redirect:/login?error=true";
                return ResponseEntity
                        .status(400)
                        .body("Not registered");

            }
        } catch (Exception e) {

            System.out.println("Ошибка при авторизации: " + e.getMessage());
//            return "redirect:/login?error=true";
            return ResponseEntity
                    .status(400)
                    .body("Not registered");
        }
    }
}
