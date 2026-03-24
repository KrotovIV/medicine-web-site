package com.github.KrotovIV.backend.interceptors;

import com.github.KrotovIV.backend.services.JwtTokenService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@RequiredArgsConstructor
public class JwtInterceptor implements HandlerInterceptor {
    private final JwtTokenService jwtTokenService;

    @Override
    public boolean preHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler
    ) {
        String token = null;

        // 1. Сначала пробуем получить токен из заголовка Authorization
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
        }

        // 2. Если в заголовке нет, пробуем получить из cookie
        if (token == null && request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("jwtToken".equals(cookie.getName())) {
                    token = cookie.getValue();
                    break;
                }
            }
        }

        // Проверяем валидность токена
        if (token != null && jwtTokenService.validateAccessToken(token)) {
            String userLogin = jwtTokenService.getUserByAccessToken(token);
            request.setAttribute("userLogin", userLogin);
            return true;
        }

        // Если токен невалидный, возвращаем 401
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        return false;
    }
}