package com.example.zubrilkaenglish.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "all_word_table")
data class Word(
    @PrimaryKey(autoGenerate = false)
    val id: Int?,
    @ColumnInfo(name = "foreignWord")
    val foreignWord:String?,
    @ColumnInfo(name = "transcription")
    val transcription:String?,
    @ColumnInfo(name = "translation")
    val translation:String?,
    @ColumnInfo(name = "groupWord")
    val groupWord:String?,
    @ColumnInfo(name = "hasVoise")
    val hasVoise: String?,
    @ColumnInfo(name = "hasImage")
    val hasImage: String?
){
    override fun toString(): String {
        return "Word ($id, '$foreignWord', $transcription, '$translation', $hasVoise, $hasImage, $groupWord)"
    }
}