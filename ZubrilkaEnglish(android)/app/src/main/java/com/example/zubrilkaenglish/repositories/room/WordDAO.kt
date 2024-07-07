package com.example.zubrilkaenglish.repositories.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.zubrilkaenglish.models.Word
import com.example.zubrilkaenglish.models.WordCard

@Dao
interface WordDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWord(wordWord: Word)
    @Query("SELECT*FROM all_word_table")
    suspend fun getAllWords(): List<Word>
//    @Query("DELETE FROM all_word_table")
//    suspend fun deleteAllWords()
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertListWords(listWords: List<Word>)
    @Query("SELECT * FROM all_word_table WHERE id = :id LIMIT 1")
    suspend fun getWordById(id: Int): Word

    /**
     * функция достанет Word из БД в виде WordCard вместе с прогрессом пользователя по этой карточке
     */
    @Query("SELECT * FROM all_word_table w LEFT JOIN progress_word c ON w.id = c.wordId WHERE w.id = :wordId")
    suspend fun getWordCardById(wordId: Int?): WordCard

    /**
     * функция вернет список всех WordCard, в том числе и тех, у которых нет(null) ProgressWord
     */
    @Query("SELECT w.*, c.* FROM all_word_table w LEFT JOIN progress_word c ON w.id = c.wordId ORDER BY w.sorting_value DESC")
    suspend fun getAllWordCards(): List<WordCard>
    @Query("SELECT translation FROM all_word_table")
    suspend fun getAllTranslations(): List<String?>

    /**
     * вернет список карточек по списку id
     */
    @Query("SELECT w.*, c.* FROM all_word_table w LEFT JOIN progress_word c ON w.id = c.wordId WHERE w.id IN (:listId)")
    suspend fun getListWordCardsBiId(listId: ArrayList<Long>?): List<WordCard>
}