package com.example.ze_adminandroid.services.retrofit

import com.example.ze_adminandroid.models.Word
import retrofit2.Response
import retrofit2.http.GET

interface WordApi {
    @GET("/ze-app/words")
    suspend fun getAllWords(): Response<List<Word>>
}