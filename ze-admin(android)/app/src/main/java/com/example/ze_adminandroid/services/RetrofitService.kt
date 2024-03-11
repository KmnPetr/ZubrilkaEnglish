package com.example.ze_adminandroid.services

import com.example.ze_adminandroid.models.Voice
import com.example.ze_adminandroid.models.Word
import com.example.ze_adminandroid.services.retrofit.RetrofitInstance
import okhttp3.ResponseBody
import retrofit2.Response

class RetrofitService private constructor(){

    companion object{
        val instance: RetrofitService by lazy { RetrofitService() }
    }

    /**
     * функция запросит с сервера список всех слов
     */
    suspend fun getAllWordsFromNetwork(): List<Word>? {
        return RetrofitInstance.wordApi.getAllWords().body()
    }

    suspend fun getVoiceDataByName(voiceName: String): Voice? {

        try {
            val response: Response<ResponseBody> = RetrofitInstance.voiceApi.getVoiceByName(voiceName)
            if (response.isSuccessful){
                val filename: String? = response.headers()["filename"]
                val byteArray = response.body()?.bytes()
                if (filename != null && byteArray != null){
                    return Voice(filename,byteArray)
                }
            }
        }catch (e: Exception){
            e.printStackTrace()
//            Toast.makeText(MyApplication.context,"ошибка подключения к серверу",Toast.LENGTH_LONG).show() //TODO ???
        }
        //на крайняк выбросит пустое значение
        return null
    }
}