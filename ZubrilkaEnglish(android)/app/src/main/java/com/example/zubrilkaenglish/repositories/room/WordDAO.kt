package com.example.zubrilkaenglish.repositories.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.zubrilkaenglish.models.Word

@Dao
interface WordDAO {
    @Insert
    suspend fun insertWord(wordWord: Word)
    @Query("SELECT*FROM all_word_table")
    suspend fun getAllWords(): List<Word>
    @Query("DELETE FROM all_word_table")
    suspend fun deleteAllWords()
    @Insert
    suspend fun insertListWords(listWords: List<Word>)
    @Query("SELECT * FROM all_word_table WHERE id = :id LIMIT 1")
    suspend fun getWordById(id: Int): Word
}