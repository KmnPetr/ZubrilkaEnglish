package com.example.ze_adminandroid.services.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.ze_adminandroid.models.Word

@Dao
interface EditedWordDAO {
    @Insert
    suspend fun saveEditableWord(word: Word)
    @Query("SELECT * FROM editable_words")
    suspend fun getAllEditedWords(): List<Word>?
}