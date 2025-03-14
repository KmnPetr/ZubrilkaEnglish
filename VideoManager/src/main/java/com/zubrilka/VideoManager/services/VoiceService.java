package com.zubrilka.VideoManager.services;

import com.zubrilka.VideoManager.controllers.validation.NotFoundException;
import com.zubrilka.VideoManager.models.Voice;
import com.zubrilka.VideoManager.repositories.VoiceRepository;
import com.zubrilka.VideoManager.util.MediaLocalStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Paths;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class VoiceService {
    private final VoiceRepository voiceRepository;
    private final MediaLocalStorage mediaLocalStorage;



    @Autowired
    public VoiceService(VoiceRepository voiceRepository, MediaLocalStorage mediaLocalStorage) {
        this.voiceRepository = voiceRepository;
        this.mediaLocalStorage = mediaLocalStorage;
    }

    /**
     * принимает wav файл конвертирует в mp3 сохраняет в локальном хранилище
     */
    @Transactional
    public UUID saveWavVoice(MultipartFile file, String text){

        UUID uuid = UUID.randomUUID();

        String localStoragePath = mediaLocalStorage.saveWavVoice(file,uuid.toString());

        Voice newVoice = new Voice(
                uuid,
                text,
                localStoragePath
        );
        return voiceRepository.save(newVoice).getUuid();
    }

    /**
     * достанет voice из локального хранилища
     */
    public BufferedInputStream getVoiceMp3(UUID uuid) throws NotFoundException {
        String localLink = voiceRepository.findById(uuid).orElseThrow(() -> new NotFoundException("Voice with uuid %s not found".formatted(uuid))).getLocal_link();
        try{
            return mediaLocalStorage.getVoiceAsMp3(localLink);
        } catch (FileNotFoundException e) {
            throw new NotFoundException("Voice with uuid %s not found".formatted(uuid));
        }
    }
}
