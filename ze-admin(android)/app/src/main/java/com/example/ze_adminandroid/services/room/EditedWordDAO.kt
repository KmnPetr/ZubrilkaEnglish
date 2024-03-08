package com.example.ze_adminandroid.services.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.ze_adminandroid.models.Word
import kotlinx.coroutines.flow.Flow

@Dao
interface EditedWordDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveEditableWord(word: Word)
    @Query("SELECT*FROM editable_words")
    fun getAllEditedWords(): Flow<List<Word>>
}