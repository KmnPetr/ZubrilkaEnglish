package com.zubrilka.zubrilkaenglish.repositories.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.zubrilka.zubrilkaenglish.models.ProgressWord
import com.zubrilka.zubrilkaenglish.models.WordCard
import com.zubrilka.zubrilkaenglish.utils.StatProgress
import kotlinx.coroutines.flow.Flow

@Dao
interface ProgressDAO {
    @Insert
    suspend fun insertProgressWord(progressWord: ProgressWord)
    @Query("SELECT * FROM progress_word INNER JOIN all_word_table ON progress_word.wordId = all_word_table.id")
    suspend fun getListWordsCards(): List<WordCard>?
    @Update
    suspend fun updateProgressWord(progressWord: ProgressWord)
    @Query("SELECT * FROM progress_word WHERE progId = :progId")
    suspend fun getProgressWordById(progId: Int?): ProgressWord?
    @Query("DELETE FROM progress_word WHERE wordId = :idWord")
    suspend fun deleteProgressByWordId(idWord: Int?)
    @Query("SELECT * FROM progress_word WHERE statProgress != :status")
    fun getAllProgressUnlearnedCards(status: String = StatProgress.LEARNED.value): Flow<List<ProgressWord>>
    @Query("SELECT * FROM progress_word WHERE statProgress != :status")
    suspend fun getAllProgressUnlearnedCards2(status: String = StatProgress.LEARNED.value): List<ProgressWord>
}