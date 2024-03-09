package com.example.ze_adminandroid.services.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.example.ze_adminandroid.models.Voice

@Dao
interface CreatedVoiceDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveVoice(voice: Voice)
}