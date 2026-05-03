package com.github.KrotovIV.frontend.controllers;

import com.github.KrotovIV.frontend.baseLogging.LoggingDecorator;
import com.github.KrotovIV.frontend.dto.MediaFilesListDto;
import com.github.KrotovIV.frontend.dto.PatientCardDtoResponse;
import com.github.KrotovIV.frontend.formatters.PatientCardFormatter;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/patient")
@RequiredArgsConstructor
public class PatientPageController {

    private final WebClient webClient;

    @Autowired
    private PatientCardFormatter patientCardFormatter;

    // Захардкоженный ID пациента
    private static final String BACKEND_API_URL = "http://127.0.0.1:8081/api";
    private final String getUsernameUrl = "http://127.0.0.1:8081/api/username";

    private String getGetPatientDataUrl(Long id) {
        return BACKEND_API_URL + "/patients/patient/" + id + "/data";
    }

    private String getMediaFilesListUrl(Long id) {
        return BACKEND_API_URL + "/patients/patient/" + id + "/mediafiles/list";
    }

    @LoggingDecorator
    @GetMapping
    public String getPatientPage(
            @CookieValue(value="jwtToken", required = false) String jwtToken,
            @RequestParam(value="id") Long id,
            Model model
    ) {
        // проверка наличия jwt-токена в куках
        boolean isAuthenticated = jwtToken != null && !jwtToken.isEmpty();

        if (!isAuthenticated) {
            return "redirect:/login";
        }

        // получение имени пользователя с бекенда
        var username = webClient.get()
                .uri(getUsernameUrl)
                .cookie("jwtToken", jwtToken)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        // Получаем список видео с бекенда
        List<VideoInfo> videos = fetchVideosFromBackend(jwtToken, id);


        // получение данных пациента с бекенда
        var patientData = webClient.get()
                .uri(getGetPatientDataUrl(id))
                .cookie("jwtToken", jwtToken)
                .retrieve()
                .bodyToMono(PatientCardDtoResponse.class)
                .block();

        var patientAvatar = patientData.avatar();
        var patientName = patientData.name();
        var patientBirthDate = patientData.birthDate();
        var patientCondition = patientData.condition();
        var patientLastVisitDate = patientData.lastVisitDate();

        String patientAge = patientCardFormatter.formatAge(patientBirthDate);

        // получение списка медиафайлов пациента с бекенда
        var mediaFilesListResponse = webClient.get()
                .uri(getMediaFilesListUrl(id))
                .cookie("jwtToken", jwtToken)
                .retrieve()
                .bodyToMono(MediaFilesListDto.class)
                .block();

        System.out.println("Recieved media filies response: " + mediaFilesListResponse.toString());

        // Передаем данные на фронтенд
        model.addAttribute("age", patientAge);
        model.addAttribute("condition", patientCondition);
        model.addAttribute("avatar", patientAvatar);
        model.addAttribute("name", patientName);
        model.addAttribute("patientId", id);
        model.addAttribute("username", username);
        model.addAttribute("videos", videos);

        return "patient";
    }

    private List<VideoInfo> fetchVideosFromBackend(String jwtToken, Long id) {
        System.out.println("Trying to fetch videos for patient" + id);
        try {
            String url = BACKEND_API_URL + "/patients/patient/" + id + "/videos";

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