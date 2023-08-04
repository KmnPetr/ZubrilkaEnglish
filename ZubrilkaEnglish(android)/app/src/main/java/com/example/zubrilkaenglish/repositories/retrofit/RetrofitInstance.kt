package com.example.zubrilkaenglish.repositories.retrofit

import com.example.zubrilkaenglish.utils.URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    private val retrofit by lazy{
        Retrofit.Builder()
            .baseUrl(URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    val wordApi:WordApi by lazy {
        retrofit.create(WordApi::class.java)
    }
    val propApi:PropApi by lazy{
        retrofit.create(PropApi::class.java)
    }
}//TODO забыл интерсептор