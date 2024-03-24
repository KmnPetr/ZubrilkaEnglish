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

    @Query("SELECT*FROM editable_words WHERE editable_words.is_ready = :isReady")
    suspend fun getNotReadyWords(isReady:Boolean):List<Word>?

    @Query("SELECT COUNT(*) FROM editable_words")
    fun getCount(): Flow<Int>
    @Query("SELECT * FROM editable_words LIMIT 1")
    suspend fun getFirstWord(): Word?
    @Query("DELETE FROM editable_words WHERE editable_words.localBaseId = :localBaseId")
    suspend fun deleteWord(localBaseId: Int?)
}