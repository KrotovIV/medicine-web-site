package com.github.KrotovIV.backend.services;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

@Service
public class DataBaseService {

    // заглушка
    private static final String DEFAULT_IMAGE_PATH = "static/images/test.jpg";

    public Resource getImage() {
        try {
            return new ClassPathResource(DEFAULT_IMAGE_PATH);
        } catch (Exception e) {
            throw new RuntimeException("Image not found", e);
        }
    }
}
