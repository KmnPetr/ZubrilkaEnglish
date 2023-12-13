package com.example.ZubrilkaEnglishServer.controllers;

import com.example.ZubrilkaEnglishServer.repositories.CheckConnectionDB;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/healthcheck")
public class HealthCheckController {

    private final CheckConnectionDB checkConnectionDB;//Костыль как есть, надо переносить проверку доступности БД в initConteiner

    @Autowired
    public HealthCheckController(CheckConnectionDB checkConnectionDB) {
        this.checkConnectionDB = checkConnectionDB;
    }

    @GetMapping()
    public ResponseEntity<String> healthCheck(){
        return new ResponseEntity<>(
                "All ok.",
                HttpStatus.OK
        );
    }
    @GetMapping("/startup")
    public ResponseEntity<String> startUpHealthCheck(){
        System.out.println("startup-healthcheck");
        try {
            checkConnectionDB.selectOne();
        }catch (Exception e){
            System.out.println("Database not available.");
            e.printStackTrace();
            return new ResponseEntity<>("Database not available.",HttpStatus.SERVICE_UNAVAILABLE);
        }

        return new ResponseEntity<>("App started.",HttpStatus.OK);
    }
}