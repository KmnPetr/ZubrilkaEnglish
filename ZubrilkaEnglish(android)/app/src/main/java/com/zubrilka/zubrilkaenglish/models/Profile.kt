package com.zubrilka.zubrilkaenglish.models

import com.google.gson.Gson
import java.sql.Timestamp

/**
 * класс для перессылки по сети данных пользователя а также для хранения в БД
 */
data class Profile(
    //это id берется с сервера
    val id: Long,
    val email:String,
    //только для первичного запроса на сервер не для хранения в ДБ
    val requestPassword:String,
    val name:String,
    var accessToken:String?,
    val refreshToken: String?,
    val created_at: String?,
    val isTempProf: Boolean? //укажет если это временный аккаунт автоматически сгенерированный
){
    fun toJson(): String {
        val gson = Gson()
        return gson.toJson(this)
    }
    companion object {
        fun toProfileObject(jsonProfile: String): Profile {
            val gson = Gson()
            return gson.fromJson(jsonProfile,Profile::class.java)
        }
    }
}

