package com.example.zeapp.services;

import com.example.zeapp.models.VoiceFile;
import com.example.zeapp.repositories.VoiceFilesRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class VoiceFileService {
    private final VoiceFilesRepository voiceFilesRepository;
    private final VoiceCache voiceCache;
    @Autowired
    public VoiceFileService(VoiceFilesRepository voiceFilesRepository, VoiceCache voiceCache) {
        this.voiceFilesRepository = voiceFilesRepository;
        this.voiceCache = voiceCache;
    }

    public Mono<VoiceFile> getVoiceByName(String name){
        if(!voiceCache.getReplcProc()){
            return voiceCache.getVoiseFile(name);
        }else {
            return voiceFilesRepository.findByFileName(name);
        }
    }


    /**
     * метод произведет замену кэша в VoiceCache
     * на вход следует передать новое значение dictionaryVersion из БД
     */
    public void replaceCache(Integer dBdicVers) {
        voiceCache.replaceCache(dBdicVers);
    }
}
