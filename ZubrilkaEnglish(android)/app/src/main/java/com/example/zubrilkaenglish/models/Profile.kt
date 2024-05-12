package com.example.zubrilkaenglish.models

/**
 * класс для перессылки по сети данных пользователя а также для хранения в БД
 */
data class Profile(
    val email:String,
    //только для первичного запроса на сервер не для хранения в ДБ
    val requestPassword:String,
    val name:String
)