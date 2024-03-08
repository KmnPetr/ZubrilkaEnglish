package com.example.ze_adminandroid.services.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.example.ze_adminandroid.models.Word

@Dao
interface EditedWordDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveEditableWord(word: Word):Unit
//    @Query("SELECT*FROM editable_words")
//    suspend fun getAllEditedWords(): List<Word>
}