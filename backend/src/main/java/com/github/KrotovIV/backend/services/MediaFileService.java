package com.github.KrotovIV.backend.services;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
@Service
public class MediaFileService {

    private static final String MEDIA_PATH = "media/patient_%d/";
    private static final String PHOTO_PATH = "photos/";
    private static final String VIDEO_PATH = "videos/";
    private static final String AUDIO_PATH = "audio/";

    /**
     * Конвертирует файл в base64 строку
     */
    private String convertFileToBase64(String filePath) throws IOException {
        ClassPathResource resource = new ClassPathResource(filePath);
        byte[] fileBytes = Files.readAllBytes(Path.of(resource.getURI()));
        return Base64.getEncoder().encodeToString(fileBytes);
    }

    /**
     * Получает список фото в base64 для пациента
     */
    public List<String> getPatientPhotos(Long patientId) {
//        try {
//            String photoPath = String.format(MEDIA_PATH + PHOTO_PATH, patientId);
//            // Для примера загружаем одно фото
//            String filePath = photoPath + "photo1.jpg";
//            return List.of(convertFileToBase64(filePath));
//        } catch (IOException e) {
//            System.out.println("Не удалось загрузить фото для пациента " + patientId + ": " + e.getMessage());
//            return List.of();
//        }
        return new ArrayList<>(List.of());
    }

    /**
     * Получает список видео в base64 для пациента
     */
    public List<String> getPatientVideos(Long patientId) {
        if (patientId == 1L)
            return new ArrayList<>(List.of("video1.mp4", "video2.mp4"));
        return new ArrayList<>(List.of());
    }

    /**
     * Получает список аудио в base64 для пациента
     */
    public List<String> getPatientAudios(Long patientId) {
//        try {
//            String audioPath = String.format(MEDIA_PATH + AUDIO_PATH, patientId);
//            // Для примера загружаем одно аудио
//            String filePath = audioPath + "audio1.mp3";
//            return List.of(convertFileToBase64(filePath));
//        } catch (IOException e) {
//            System.out.println("Не удалось загрузить аудио для пациента " + patientId + ": " + e.getMessage());
//            return List.of();
//        }
        return new ArrayList<>(List.of());
    }

    /**
     * Генерирует текстовые заметки для пациента
     */
    public List<String> getPatientTextNotes(Long patientId) {
        // Временные текстовые заметки для теста

        return new ArrayList<>(List.of());
    }
}