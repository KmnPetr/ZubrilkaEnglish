package com.example.ZubrilkaEnglishServer.repositories;

import com.example.ZubrilkaEnglishServer.models.Word;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WordsRepositories extends JpaRepository<Word,Integer> {
}
