package com.example.zubrilkaenglish.repositories.room

import androidx.room.Dao
import androidx.room.Query
import com.example.zubrilkaenglish.models.PropModel

@Dao
interface PropDAO {
    @Query("SELECT*FROM prop_table WHERE prop_table.`key`='dictionary_version'")
    suspend fun getDictionaryVersion(): PropModel?
    @Query("UPDATE prop_table SET value=:newDictionaryVersion WHERE prop_table.`key` ='dictionary_version'")
    suspend fun updateDictionaryVersion(newDictionaryVersion: String?)
    @Query("INSERT INTO prop_table ('key', value) VALUES ('dictionary_version', :newDictionaryVersion)")
    suspend fun insertNewDictionaryVersion(newDictionaryVersion: String?)
}