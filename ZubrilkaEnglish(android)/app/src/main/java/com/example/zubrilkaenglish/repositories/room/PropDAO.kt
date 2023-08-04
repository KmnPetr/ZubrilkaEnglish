package com.example.zubrilkaenglish.repositories.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.zubrilkaenglish.models.PropModel
import okhttp3.Response

@Dao
interface PropDAO {
    @Query("SELECT*FROM prop_table WHERE prop_table.`key`='update_at'")
    suspend fun getUpdatedAt(): PropModel?
    @Query("UPDATE prop_table SET value=:newUpdateAt WHERE 'key'='update_at'")
    suspend fun updateUpdatedAt(newUpdateAt: String?)
    @Query("INSERT INTO prop_table ('key', value) VALUES ('update_at', :newUpdatedAt)")
    suspend fun insertNewUpdatedAt(newUpdatedAt: String?)
}