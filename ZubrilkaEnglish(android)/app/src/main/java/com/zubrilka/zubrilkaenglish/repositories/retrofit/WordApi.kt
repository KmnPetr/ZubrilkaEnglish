package com.zubrilka.zubrilkaenglish.repositories.retrofit

import com.zubrilka.zubrilkaenglish.models.Word
import retrofit2.Response
import retrofit2.http.GET

interface WordApi {
    @GET("/words")
    suspend fun getAllWords(): Response<List<Word>>
    @GET("/words/initialTrainingList")
    suspend fun getInitialTrainingList(): Response<List<Word>>
}