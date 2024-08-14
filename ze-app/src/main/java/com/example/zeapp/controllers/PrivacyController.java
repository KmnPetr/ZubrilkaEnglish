package com.example.zeapp.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/privacy")
public class PrivacyController {

    private final ResourceLoader resourceLoader;
    @Autowired
    public PrivacyController(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }


    @GetMapping(value = "", produces = MediaType.TEXT_HTML_VALUE)
    public Mono<String> getFile() {
        // Укажите путь к файлу
        String filePath = "classpath:static/privacy.html";

        return Mono.fromSupplier(() -> {
            try {
                // Загружаем файл как ресурс
                Resource resource = resourceLoader.getResource(filePath);

                // Читаем содержимое файла
                Path path = Paths.get(resource.getURI());
                return new String(Files.readAllBytes(path));
            } catch (Exception e) {
                // Обработка ошибок
                throw new RuntimeException("Error reading file", e);
            }
        });
    }
}