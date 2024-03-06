package com.example.ze_adminandroid.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "editable_words")
data class Word(

    //это поле для адекватной работы с местной базой данных конкретно в этом приложении,
    // при этом строго запрещено ей менять id приходящее с сервера,
    // и новые созданные Word отправляются на сервер с значением id = null
    @PrimaryKey
    val localBaseId: Int?,


    @ColumnInfo(name = "id")
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
)
