package com.example.zubrilkaenglish.repositories.retrofit

import com.example.zubrilkaenglish.utils.URL
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.create

object RetrofitInstance {
    val interceptor: HttpLoggingInterceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
    val okHttpClient: OkHttpClient = OkHttpClient.Builder().addInterceptor(interceptor).build()


    private val jsonRetrofit by lazy{
        Retrofit.Builder()
            .baseUrl(URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
    }

    //этот инстанс ретрофита принимает просто байты не пытаясь их распарсить в json
    private val voiceRetrofit by lazy{
        Retrofit.Builder()
            .baseUrl(URL)
//            .client(okHttpClient)
            .build()
    }
    val wordApi:WordApi by lazy {
        jsonRetrofit.create(WordApi::class.java)
    }
    val propApi:PropApi by lazy{
        jsonRetrofit.create(PropApi::class.java)
    }
    val voiceApi:VoiceApi by lazy{
        voiceRetrofit.create(VoiceApi::class.java)
    }
    val profileApi:ProfileApi by lazy {
        jsonRetrofit.create(ProfileApi::class.java)
    }
    val statisticsApi: StatisticsApi by lazy {
        jsonRetrofit.create(StatisticsApi::class.java)
    }
}