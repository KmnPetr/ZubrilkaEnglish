package com.zubrilka.zubrilkaenglish.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "voice_table")
class Voice(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "name")
    val voiceName: String,
    @ColumnInfo(name = "data")
    var voiceData: ByteArray?
) {
}