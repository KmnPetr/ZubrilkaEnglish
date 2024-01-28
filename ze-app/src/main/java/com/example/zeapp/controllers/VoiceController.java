package com.example.zeapp.controllers;

import com.example.zeapp.models.VoiceFile;
import com.example.zeapp.services.VoiceFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import org.springframework.http.HttpHeaders;

@RestController
@RequestMapping("/voice")
public class VoiceController {
    private final VoiceFileService voiceFileService;
    @Autowired
    public VoiceController(VoiceFileService voiceFileService) {
        this.voiceFileService = voiceFileService;
    }

    /**
     * метод вернет аудиофайл как json обьект
     */
    @GetMapping("/{name}")
    public Mono<VoiceFile> getVoiceByName(@PathVariable String name){
        return voiceFileService.getVoiceByName(name);
    }

    /**
     * метод не только вернет файл, но и заставит браузер его скачать
     */
    @GetMapping("/f/{name}")
    public Mono<ResponseEntity<byte[]>> getVoiceAsFileByName(@PathVariable String name){
        return voiceFileService
                .getVoiceByName(name)
                .map(it->
                    ResponseEntity
                            .ok()
                            .contentType(MediaType.APPLICATION_OCTET_STREAM)
                            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename="+it.getFileName())
                            .body(it.getFileData()));
    }
}
