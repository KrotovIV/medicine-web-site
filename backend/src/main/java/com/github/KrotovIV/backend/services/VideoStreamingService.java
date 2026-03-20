package com.github.KrotovIV.backend.services;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import lombok.extern.java.Log;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service
@Log
public class VideoStreamingService {

    private static final String MEDIA_PATH = "media/patient_%d/videos/";

    /**
     * Получает видео файл как Resource
     */
    public Resource getVideoResource(Long patientId, String videoName) throws IOException {
        String videoPath = String.format(MEDIA_PATH, patientId) + videoName;
        return new ClassPathResource(videoPath);
    }

    /**
     * Проверяет существование видео
     */
    public boolean videoExists(Long patientId, String videoName) {
        String videoPath = String.format(MEDIA_PATH, patientId) + videoName;
        try {
            Resource resource = new ClassPathResource(videoPath);
            return resource.exists() && resource.isReadable();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Получает список доступных видео для пациента
     */
    public List<String> getAvailableVideos(Long patientId) {
        try {
            String videoDir = String.format(MEDIA_PATH, patientId);
            // В реальном приложении здесь нужно сканировать директорию
            // Пока возвращаем заглушку
            return List.of("video1.mp4", "video2.mp4");
        } catch (Exception e) {
            log.warning("Не удалось получить список видео для пациента " + patientId);
            return List.of();
        }
    }

    /**
     * Получает размер видео файла
     */
    public long getVideoSize(Long patientId, String videoName) throws IOException {
        Resource resource = getVideoResource(patientId, videoName);
        return resource.contentLength();
    }

    /**
     * Получает MIME тип видео
     */
    public String getVideoContentType(String videoName) {
        String extension = videoName.substring(videoName.lastIndexOf(".") + 1).toLowerCase();
        return switch (extension) {
            case "mp4" -> "video/mp4";
            case "webm" -> "video/webm";
            case "ogg" -> "video/ogg";
            case "mpeg" -> "video/mpeg";
            default -> "video/mp4";
        };
    }
}