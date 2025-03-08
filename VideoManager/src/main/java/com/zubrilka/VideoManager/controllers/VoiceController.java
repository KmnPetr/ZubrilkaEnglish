package com.zubrilka.VideoManager.controllers;

import com.zubrilka.VideoManager.services.VoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
@RequestMapping("/api/voice")
public class VoiceController {
    private final VoiceService voiceService;
    @Autowired
    public VoiceController(VoiceService voiceService) {
        this.voiceService = voiceService;
    }

    // Указываем абсолютный путь к папке, где будут сохраняться файлы
    private static final String UPLOAD_DIR = "C:/Users/Petr/git/VideoManagerStore/voice/";

    @PostMapping("/save_wav_voice")
    public ResponseEntity<UUID> saveWavVoice(
            @RequestParam("file") MultipartFile file,
            @RequestParam("text") String text) {

        return ResponseEntity.ok(voiceService.saveWavVoice(file,text));
    }
}
