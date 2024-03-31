package com.example.zeauth.controllers;

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


    /**
     * livenessProbe for kubernetes
     */
    @GetMapping()
    public Mono<String> healthCheck(){
        return Mono.just("All ok.");
    }

    /**
     * startupProbe for kubernetes
     */
    @GetMapping("/startup")
    public Mono<ResponseEntity<String>> startUpHealthCheck(){
        log.info("startup-healthcheck called");

        if (true){ //TODO требуется проверка доступности необходимых для запуска сервисов БД и др.
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