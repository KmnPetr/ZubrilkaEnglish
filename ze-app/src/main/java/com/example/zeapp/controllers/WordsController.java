package com.example.zeapp.controllers;

import com.example.zeapp.models.Word;
import com.example.zeapp.services.WordsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/words")
public class WordsController {
    private final WordsService wordsService;
    @Autowired
    public WordsController(WordsService wordsService) {
        this.wordsService = wordsService;
    }

    @GetMapping
    public Flux<Word> getAllWords(){
        return wordsService.getAllWords();
    }

    /**
     * выдаст список слов для первоначальной установки их в качестве учебных
     * вызывается фронтом при первоначальной установке приложения
     */
    @GetMapping("/initialTrainingList")
    public Flux<Word> initialTrainingList(){
        return wordsService.getInitialTrainingList();
    }
}
