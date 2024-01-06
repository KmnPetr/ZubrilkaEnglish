package com.example.zeapp.repositories;


import com.example.zeapp.models.Word;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface WordsRepository extends ReactiveCrudRepository<Word,Integer> {
}
