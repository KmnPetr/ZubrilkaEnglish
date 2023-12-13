package com.example.ZubrilkaEnglishServer.repositories;

import com.example.ZubrilkaEnglishServer.models.Word;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

//Однозначно это костыль, чтобы его убрать нужно проверку доступности БД перенести в initConteiner пода
@Repository
public interface CheckConnectionDB extends JpaRepository<Word,Integer> {
    @Query(value = "SELECT 1", nativeQuery = true)
    Integer selectOne();
}
