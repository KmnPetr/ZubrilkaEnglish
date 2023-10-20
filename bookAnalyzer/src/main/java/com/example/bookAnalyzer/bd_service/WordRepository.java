package com.example.bookAnalyzer.bd_service;

import com.example.bookAnalyzer.models.Word;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
interface WordRepository extends JpaRepository<Word,Integer> {

}
