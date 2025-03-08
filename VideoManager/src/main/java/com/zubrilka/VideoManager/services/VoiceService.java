package com.zubrilka.VideoManager.services;

import com.zubrilka.VideoManager.models.Voice;
import com.zubrilka.VideoManager.repositories.VoiceRepository;
import com.zubrilka.VideoManager.util.MediaLocalStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
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

}
