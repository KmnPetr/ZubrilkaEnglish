package com.example.WordsManager.controllers;

import com.example.WordsManager.services.WordsCache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequestMapping("/healthcheck")
public class  HealthCheckController {
    private final WordsCache wordsCache;
//    private final VoiceCache voiceCache;

    @Autowired
    public HealthCheckController(WordsCache wordsCache/*, VoiceCache voiceCache*/) {
        this.wordsCache = wordsCache;
//        this.voiceCache = voiceCache;
    }

    /**
     * livenessProbe for kubernetes
     */
    @GetMapping()
    public Mono<String> healthCheck(){
        return Mono.just("All ok.");
    }

    /**
     * startupProbe for kubernetes
     * checking the initial download
     * by the value of the dictionaryVersion from the object,
     * WordsCache will understand the state of readiness of the application to provide information
     */
    @GetMapping("/startup")
    public Mono<ResponseEntity<String>> startUpHealthCheck(){
        log.info("startup-healthcheck called");

        Integer wordsDicVers = wordsCache.getDictionaryVersion();
//        Integer voiceDicVers = voiceCache.getDictionaryVersion();
        if (wordsDicVers>0/*&&voiceDicVers>0*/){
            return Mono.just(
                    ResponseEntity
                            .ok()
                            .body("App is ready.")
            );
        }else return Mono.just(
                ResponseEntity
                        .status(HttpStatus.SERVICE_UNAVAILABLE)
                        .body("App is not ready yet..")
        );
    }
}