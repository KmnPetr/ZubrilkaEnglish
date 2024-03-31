package com.example.WordsManager.controllers;

import com.example.WordsManager.services.BackupWordService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/backup")
public class BackupController {
    private final BackupWordService backupWordService;
    @Autowired
    public BackupController(BackupWordService backupWordService) {
        this.backupWordService = backupWordService;
    }

    @PostMapping("/save-words")
    public void saveWords(){
        backupWordService.saveWords();
    }
}
