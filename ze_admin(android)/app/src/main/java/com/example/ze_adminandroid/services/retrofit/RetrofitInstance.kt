package com.example.ze_adminandroid.services.retrofit

import com.example.ze_adminandroid.util.URL
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object RetrofitInstance {
    val interceptor: HttpLoggingInterceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
    val okHttpClient: OkHttpClient = OkHttpClient.Builder().addInterceptor(interceptor).build()


    private val jsonRetrofit by lazy{
        Retrofit.Builder()
            .baseUrl(URL)
            .addConverterFactory(GsonConverterFactory.create())
//            .client(okHttpClient)
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
//    val propApi:PropApi by lazy{
//        jsonRetrofit.create(PropApi::class.java)
//    }
//    val voiceApi:VoiceApi by lazy{
//        voiceRetrofit.create(VoiceApi::class.java)
//    }
}