package com.zubrilka.VideoManager.controllers;

import com.zubrilka.VideoManager.controllers.validation.NotFoundException;
import com.zubrilka.VideoManager.services.VoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.BufferedInputStream;
import java.io.IOException;
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
            @RequestParam("text") String text,
            @RequestParam("voice") String voice,
            @RequestParam("sex") String sex) {

        return ResponseEntity.ok(voiceService.saveWavVoice(file,text,voice,sex));
    }

    @GetMapping("/get_mp3/{uuid}")
    public ResponseEntity<StreamingResponseBody> getVoiceAsMp3(@PathVariable UUID uuid) throws NotFoundException {
        BufferedInputStream voiceMp3 = voiceService.getVoiceMp3(uuid);

        StreamingResponseBody stream = outputStream -> {
            try {
                byte[] buffer = new byte[8192]; // Буфер для чтения данных
                int bytesRead;
                while ((bytesRead = voiceMp3.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
            } catch (IOException e) {
                throw new RuntimeException("Ошибка при передаче MP3", e);
            } finally {
                try {
                    voiceMp3.close(); // Закрытие потока вручную, если необходимо
                } catch (IOException e) {
                    // Логирование ошибки при закрытии потока
                }
            }
        };

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, "audio/mpeg");
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"file.mp3\"");

        return new ResponseEntity<>(stream, headers, HttpStatus.OK);
    }
}
