package com.example.ZubrilkaEnglishServer.controllers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/healthcheck")
public class HealthCheckController {
    @Value("${spring.datasource.url}")
    private String url;
    @Value("${spring.datasource.password}")
    private String password;

    @GetMapping()
    public ResponseEntity<String> healthCheck(){

        return new ResponseEntity<>(
                "All ok.   URL = "+url+"   password="+password,
                HttpStatus.OK
        );
    }
}