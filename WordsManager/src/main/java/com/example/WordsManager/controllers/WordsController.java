package com.example.WordsManager.controllers;

import com.example.WordsManager.models.Word;
import com.example.WordsManager.services.WordsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

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

    @GetMapping("/{idWord}")
    public Mono<Word> getWordById(@PathVariable Integer idWord){
        return wordsService.getWordById(idWord);
    }

    /**
     * метод принимает заявку на обновление или создание нового word
     */
    @PostMapping("/update")
    public void updateWord(@RequestBody Word word){
        System.out.println(word);
    }
}
