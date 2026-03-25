package com.github.KrotovIV.frontend.controllers;

import com.github.KrotovIV.frontend.baseLogging.LoggingDecorator;
import com.github.KrotovIV.frontend.dto.PatientCardDtoResponse;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/patient")
@RequiredArgsConstructor
public class PatientPageController {

    private final WebClient webClient;

    // Захардкоженный ID пациента
    private static final Long PATIENT_ID = 1L;
    private static final String BACKEND_API_URL = "http://127.0.0.1:8081/api";


    @LoggingDecorator
    @GetMapping
    public String getPatientPage(@CookieValue(value="jwtToken", required = false) String jwtToken, Model model) {
        // проверка наличия jwt-токена в куках
        boolean isAuthenticated = jwtToken != null && !jwtToken.isEmpty();

        if (!isAuthenticated) {
            return "redirect:/login";
        }

        // Получаем список видео с бекенда
        List<VideoInfo> videos = fetchVideosFromBackend(jwtToken);

        // Передаем данные на фронтенд
        model.addAttribute("patientId", PATIENT_ID);
        model.addAttribute("username", "Доктор"); // Заглушка для имени пользователя
        model.addAttribute("videos", videos);

        return "patient";
    }

    private List<VideoInfo> fetchVideosFromBackend(String jwtToken) {
        try {
            String url = BACKEND_API_URL + "/patients/patient/" + PATIENT_ID + "/videos";

            System.out.println("url: " + url);

            String[] videoFileNames = webClient.get()
                    .uri(url)
                    .cookie("jwtToken", jwtToken)
                    .retrieve()
                    .bodyToMono(String[].class)
                    .block();

            System.out.println("videoFileNames: " + Arrays.toString(videoFileNames));

            if (videoFileNames == null || videoFileNames.length == 0) {
                return new ArrayList<>();
            }

            List<VideoInfo> videos = Arrays.stream(videoFileNames).map(
                    filename -> VideoInfo.builder()
                            .filename(filename)
                            .streamUrl("http://127.0.0.1:8081/api/patients/patient/1/videos/" + filename)
                            .createdAt("2024-03-20 10:00:00")
                            .description("Видео пациента")
                            .tags(List.of("видео"))
                            .build()
            ).toList();

            return videos;

        } catch (Exception e) {
            System.err.println("Ошибка при загрузке видео с бекенда: " + e.getMessage());
            e.printStackTrace(); // Добавьте это для более детальной информации об ошибке
            return new ArrayList<>();
        }
    }

    // Внутренний класс для представления информации о видео
    @Builder
    public record VideoInfo (
        Long id,
        String filename,
        String createdAt,
        String description,
        List<String> tags,
        String streamUrl
    ) {}
}