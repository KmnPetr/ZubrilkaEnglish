package com.example.zubrilkaenglish.repositories.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.zubrilkaenglish.models.Memo
import kotlinx.coroutines.flow.Flow

@Dao
interface MemoDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNewMemo(memo: Memo): Long
    @Query("SELECT*FROM memo_table")
    fun getAllMemos(): Flow<List<Memo>>
    @Delete
    suspend fun deleteMemo(memo: Memo)
    @Query("SELECT * FROM memo_table WHERE memo_table.id = :memoId")
    suspend fun getMemoById(memoId: Long): Memo?
}