package com.example.ze_adminandroid.services.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.ze_adminandroid.models.Voice
import kotlinx.coroutines.flow.Flow

@Dao
interface CreatedVoiceDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveVoice(voice: Voice)
    @Query("SELECT*FROM voice_table WHERE voice_table.name = :name")
    suspend fun getVoiceByName(name: String): Voice?


    //вернет количество сущностей
    @Query("SELECT COUNT(*) FROM voice_table")
    fun getCount(): Flow<Int>

    //выдаст первое имеющееся voice
    @Query("SELECT * FROM voice_table LIMIT 1")
    suspend fun getFirstVoice(): Voice?
}