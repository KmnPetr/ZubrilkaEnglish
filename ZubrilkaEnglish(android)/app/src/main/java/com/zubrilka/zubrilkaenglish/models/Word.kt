package com.zubrilka.zubrilkaenglish.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "all_word_table")
data class Word(
    @PrimaryKey(autoGenerate = false)
    val id: Int,
    @ColumnInfo(name = "foreignWord")
    val foreignWord:String?,
    @ColumnInfo(name = "transcription")
    val transcription:String?,
    @ColumnInfo(name = "translation")
    val translation:String?,
    @ColumnInfo(name = "description")
    val description:String?,
    @ColumnInfo(name = "topic")
    val topic:String,
    @ColumnInfo(name = "link_voice")
    val link_voice: String?,
    @ColumnInfo(name = "link_image")
    val link_image: String?,
    @ColumnInfo(name = "sorting_value")
    val sorting_value: Int
){
    override fun toString(): String {
        return "Word $id, $foreignWord, $transcription, $translation, $description, $topic, $link_voice, $link_image"
    }
}