package com.example.bookAnalyzer.bd_service;

import com.example.bookAnalyzer.models.Word;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class WordServise {
    private final WordRepository wordRepository;

    @Autowired
    public WordServise(WordRepository wordRepository) {
        this.wordRepository = wordRepository;
    }

    public List<Word> getAllWordsFromBd(){
        return wordRepository.findAll();
    }
}
