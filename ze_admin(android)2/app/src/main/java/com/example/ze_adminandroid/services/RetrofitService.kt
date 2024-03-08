package com.example.ze_adminandroid.services

import com.example.ze_adminandroid.models.Word
import com.example.ze_adminandroid.services.retrofit.RetrofitInstance

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
}