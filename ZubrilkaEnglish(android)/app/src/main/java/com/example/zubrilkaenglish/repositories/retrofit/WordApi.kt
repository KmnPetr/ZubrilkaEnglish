package com.example.zubrilkaenglish.repositories.retrofit

import com.example.zubrilkaenglish.models.Word
import retrofit2.Response
import retrofit2.http.GET

interface WordApi {
@GET("/words")
suspend fun getAllWords(): Response<List<Word>>
}