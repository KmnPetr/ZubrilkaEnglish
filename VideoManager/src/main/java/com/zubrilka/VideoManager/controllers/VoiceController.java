package com.zubrilka.VideoManager.controllers;

import com.zubrilka.VideoManager.controllers.validation.NotFoundException;
import com.zubrilka.VideoManager.dto.SimilarVoiceRequestDto;
import com.zubrilka.VideoManager.dto.VoiceDto;
import com.zubrilka.VideoManager.models.Voice;
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
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/voice")
public class VoiceController {
    private final VoiceService voiceService;
    @Autowired
    public VoiceController(VoiceService voiceService) {
        this.voiceService = voiceService;
    }


    /**
     * запрос на список voice ранее озвученных схожих по тексту
     * videoInfo_uuid позволит дать дополнительную информацию по сортировке
     * с целью дать приоритет актерам озвучки ранее использованных в данном переводе видео
     */
    @PostMapping("/list_similar_voices")
    public List<VoiceDto> getListSimilarVoices(@RequestBody SimilarVoiceRequestDto request) throws NotFoundException {
        return voiceService.findSimilarVoices(request.getText(), request.getTranslation_uuid());
    }

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
