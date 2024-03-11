package com.example.ze_adminandroid.services.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.ze_adminandroid.models.PropModel


@Dao
interface PropDAO {
    @Query("SELECT*FROM prop_table WHERE prop_table.`key`='last-host'")
    suspend fun getLastHost(): PropModel?
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNewLastHost(prop: PropModel)
}