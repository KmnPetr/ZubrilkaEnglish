package com.example.zeapp.controllers;

import com.example.zeapp.models.VoiceFile;
import com.example.zeapp.services.VoiceFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/voice")
public class VoiceController {
    private final VoiceFileService voiceFileService;
    @Autowired
    public VoiceController(VoiceFileService voiceFileService) {
        this.voiceFileService = voiceFileService;
    }

    @GetMapping("/{name}")
    public Mono<VoiceFile> getVoiceByName(@PathVariable String name){
        return voiceFileService.getVoiceByName(name);
    }
}
