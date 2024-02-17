package com.example.zubrilkaenglish.repositories.retrofit

import com.example.zubrilkaenglish.utils.URL
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

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



    val interceptor: HttpLoggingInterceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
    val okHttpClient: OkHttpClient = OkHttpClient.Builder().addInterceptor(interceptor).build()

    private val voiceRetrofit by lazy{
        Retrofit.Builder()
            .baseUrl(URL)
            .client(okHttpClient)
            .build()
    }
    val voiceApi:VoiceApi by lazy{
        voiceRetrofit.create(VoiceApi::class.java)
    }
}