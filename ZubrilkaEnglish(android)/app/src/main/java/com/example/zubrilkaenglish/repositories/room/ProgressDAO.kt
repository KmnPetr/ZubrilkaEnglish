package com.example.zubrilkaenglish.repositories.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.zubrilkaenglish.models.ProgressWord
import com.example.zubrilkaenglish.models.WordWithProgress

@Dao
interface ProgressDAO {
    @Insert
    suspend fun insertProgressWord(progressWord: ProgressWord)
    @Query("SELECT * FROM progress_word INNER JOIN all_word_table ON progress_word.wordId = all_word_table.id")
    suspend fun getAllWordsWithProgress(): List<WordWithProgress>?
}