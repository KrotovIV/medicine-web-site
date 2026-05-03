package com.github.KrotovIV.backend.controllers;

import com.github.KrotovIV.backend.PartialContentResource;
import com.github.KrotovIV.backend.baseLogging.LoggingDecorator;
import com.github.KrotovIV.backend.dto.MediaFilesListDto;
import com.github.KrotovIV.backend.dto.PatientCardDtoResponse;
import com.github.KrotovIV.backend.services.DataBaseService;
import com.github.KrotovIV.backend.services.MediaFileService;
import com.github.KrotovIV.backend.services.VideoStreamingService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;


import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/patients")
@RequiredArgsConstructor
@Log
public class PatientsDataController {

    private final DataBaseService dataBaseService;
    private final MediaFileService mediaFileService;

    private final VideoStreamingService videoStreamingService;

    @LoggingDecorator
    @GetMapping("/list")
    public ResponseEntity<List<PatientCardDtoResponse>> getPatientsCardsList(
            HttpServletRequest request
    ) {
        var userLogin = request.getAttribute("userLogin");
        String userLoginString = (String) userLogin;
        var patientsList = dataBaseService.getPatients(userLoginString);
        return ResponseEntity.ok(patientsList);
    }

    @LoggingDecorator
    @GetMapping("patient/{id}/data")
    public ResponseEntity<PatientCardDtoResponse> getPatientData(
            @PathVariable("id") Long patientId
    ) {
        var patientData = dataBaseService.getPatientById(patientId);
        if (patientData.isEmpty())
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok(patientData.get());
    }

    @LoggingDecorator
    @PostMapping("/patient/add")
    public ResponseEntity<?> addPatient(
            @RequestParam("name") String name,
            @RequestParam("emoji") String emoji,
            @RequestParam("birthDate") LocalDate birthDate,
            @RequestParam("condition") String condition,
            HttpServletRequest request
    ) {
        var userLogin = request.getAttribute("userLogin");
        String userLoginString = (String) userLogin;
       return dataBaseService.addPatient(name, emoji, birthDate, condition, userLoginString);
    }

    @LoggingDecorator
    @GetMapping("/patient/{id}/mediafiles/list")
    public ResponseEntity<MediaFilesListDto> getPatientMediaFilesList(
            @PathVariable("id") Long patientId
    ) {
        // получаем и преобразуем список видео
        var videoList = mediaFileService.getPatientVideos(patientId).stream().map(
                item -> new MediaFilesListDto.MediaFileInfo(item, true)
        ).toList();

        // получаем и преобразуем список аудио
        var audioList = mediaFileService.getPatientAudios(patientId).stream().map(
                item -> new MediaFilesListDto.MediaFileInfo(item, true)
        ).toList();

        // получаем и преобразуем список фото
        var photoList = mediaFileService.getPatientPhotos(patientId).stream().map(
                item -> new MediaFilesListDto.MediaFileInfo(item, false)
        ).toList();


        // Возвращаем результат
        return ResponseEntity.ok(MediaFilesListDto.builder()
                .photosNamesList(photoList)
                .audiosNamesList(audioList)
                .videosNameslist(videoList)
                .build()
        );
    }


    @GetMapping("/patient/{id}/videos/{videoName}")
    public ResponseEntity<Resource> streamVideo(
            @PathVariable("id") Long patientId,
            @PathVariable("videoName") String videoName,
            @RequestHeader(value = "Range", required = false) String rangeHeader) {

        log.info("Запрос видео для пациента " + patientId + ": " + videoName);

        // Проверяем существование пациента
        if (dataBaseService.getPatientById(patientId).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Пациент не найден");
        }

        // Проверяем существование видео
        if (!videoStreamingService.videoExists(patientId, videoName)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Видео не найдено");
        }

        try {
            Resource video = videoStreamingService.getVideoResource(patientId, videoName);
            String contentType = videoStreamingService.getVideoContentType(videoName);
            long videoSize = videoStreamingService.getVideoSize(patientId, videoName);

            // Если нет Range заголовка, отдаем весь файл
            if (rangeHeader == null) {
                return ResponseEntity.ok()
                        .contentType(MediaType.parseMediaType(contentType))
                        .contentLength(videoSize)
                        .body(video);
            }

            // Обработка частичного контента (для стриминга)
            return handleRangeRequest(video, rangeHeader, contentType, videoSize);

        } catch (IOException e) {
            log.severe("Ошибка при стриминге видео: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Ошибка при загрузке видео");
        }
    }

    /**
     * Эндпоинт для получения списка доступных видео пациента
     */
    @GetMapping("/patient/{id}/videos")
    public ResponseEntity<List<String>> getPatientVideos(@PathVariable("id") Long patientId) {
        if (dataBaseService.getPatientById(patientId).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Пациент не найден");
        }

        List<String> videos = videoStreamingService.getAvailableVideos(patientId);
        return ResponseEntity.ok(videos);
    }

    private ResponseEntity<Resource> handleRangeRequest(
            Resource video,
            String rangeHeader,
            String contentType,
            long videoSize) {

        try {
            // Парсим Range заголовок
            String[] ranges = rangeHeader.replace("bytes=", "").split("-");
            long start = Long.parseLong(ranges[0]);
            long end = ranges.length > 1 ? Long.parseLong(ranges[1]) : videoSize - 1;

            if (start > end || start >= videoSize) {
                return ResponseEntity.status(HttpStatus.REQUESTED_RANGE_NOT_SATISFIABLE)
                        .header("Content-Range", "bytes */" + videoSize)
                        .build();
            }

            long contentLength = end - start + 1;

            return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT)
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_RANGE,
                            String.format("bytes %d-%d/%d", start, end, videoSize))
                    .contentLength(contentLength)
                    .body(new PartialContentResource(video, start, end));

        } catch (NumberFormatException e) {
            log.warning("Некорректный Range заголовок: " + rangeHeader);
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .contentLength(videoSize)
                    .body(video);
        }
    }


}