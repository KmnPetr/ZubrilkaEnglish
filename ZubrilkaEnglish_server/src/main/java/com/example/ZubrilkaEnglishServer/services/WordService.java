package com.example.ZubrilkaEnglishServer.services;

import com.example.ZubrilkaEnglishServer.models.Word;
import com.example.ZubrilkaEnglishServer.repositories.WordsRepositories;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class WordService {
    private final WordsRepositories wordsRepositories;
    @Autowired
    public WordService(WordsRepositories wordsRepositories) {
        this.wordsRepositories = wordsRepositories;
    }

    public List<Word>findAll(){
        return wordsRepositories.findAll();
    }
    @Transactional
    public void save(Word word){
        wordsRepositories.save(word);
    }
    @Transactional
    public void delete(int id){
        wordsRepositories.deleteById(id);
    }
}
