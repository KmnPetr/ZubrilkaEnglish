package com.example.zubrilkaenglish.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.zubrilkaenglish.utils.StatProgress

@Entity(
    tableName = "progress_word",
    foreignKeys = [ForeignKey(entity = Word::class, parentColumns = ["id"], childColumns = ["wordId"])],
    indices = [Index(value = ["wordId"], unique = true)]
    )
data class ProgressWord(
    @PrimaryKey(autoGenerate = true)
    val progId: Int?,
    @ColumnInfo(name = "wordId")
    val wordId: Int,
    @ColumnInfo(name = "numCorrAnsv")
    var numCorrAnsv: Int = 0,
    @ColumnInfo(name = "statProgress")
    var statProgress: String = StatProgress.NEW.value,
    @ColumnInfo(name = "sleepTime")
    var sleepTime: String?
)
