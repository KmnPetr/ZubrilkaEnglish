package com.example.zeapp.services;

import com.example.zeapp.models.VoiceFile;
import com.example.zeapp.repositories.VoiceFilesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class VoiceFileService {
    private final VoiceFilesRepository voiceFilesRepository;
    @Autowired
    public VoiceFileService(VoiceFilesRepository voiceFilesRepository) {
        this.voiceFilesRepository = voiceFilesRepository;
    }

    public Mono<VoiceFile> getVoiceByName(String name){
        return voiceFilesRepository.findByFileName(name);
    }



}
