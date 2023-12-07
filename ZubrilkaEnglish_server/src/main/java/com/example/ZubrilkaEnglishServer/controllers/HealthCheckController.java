package com.example.ZubrilkaEnglishServer.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/healthcheck")
public class HealthCheckController {

    @GetMapping()
    public ResponseEntity<String> healthCheck(){
        return new ResponseEntity<>("All ok.", HttpStatus.OK);
    }
}