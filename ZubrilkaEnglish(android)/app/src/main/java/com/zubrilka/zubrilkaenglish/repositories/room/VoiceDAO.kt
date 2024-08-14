package com.zubrilka.zubrilkaenglish.repositories.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.zubrilka.zubrilkaenglish.models.Voice

@Dao
interface VoiceDAO {
    @Query("SELECT*FROM voice_table WHERE voice_table.name = :name")
    suspend fun getVoiceByName(name: String): Voice?
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNewVoice(voice: Voice)
}