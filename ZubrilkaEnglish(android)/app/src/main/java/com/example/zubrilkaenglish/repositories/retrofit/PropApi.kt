package com.example.zubrilkaenglish.repositories.retrofit

import com.example.zubrilkaenglish.models.PropModel
import retrofit2.Response
import retrofit2.http.GET

interface PropApi {
    @GET("/properties/get_dictionary_version")
    suspend fun getDictionaryVersion():Response<PropModel>
}