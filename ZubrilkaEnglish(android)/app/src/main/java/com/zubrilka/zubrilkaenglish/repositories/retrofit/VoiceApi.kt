package com.zubrilka.zubrilkaenglish.repositories.retrofit

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface VoiceApi {
    @GET("/voice/byte/{name}")
    suspend fun getVoiceByName(@Path("name") name: String): Response<ResponseBody>
}