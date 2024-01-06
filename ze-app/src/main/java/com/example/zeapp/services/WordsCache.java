package com.example.zeapp.services;

import com.example.zeapp.models.Word;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Data
public class WordsCache {
    private List<Word> listAllWords = new ArrayList<>();
    private Integer dictionaryVersion = 0;

    public WordsCache() {
       testFillCache();
    }

    /**
     * тестовое заполнение кэша
     * Ориентировано на версию словаря 0.
     */
    private void testFillCache(){
        listAllWords.add(
                new Word(
                        0,
                        "testWord",
                        "[testWord]",
                        "тестовое Слово",
                        "тестовое Слово",
                        null,
                        null,
                        "тестовая группа",
                        0));
    }
}
