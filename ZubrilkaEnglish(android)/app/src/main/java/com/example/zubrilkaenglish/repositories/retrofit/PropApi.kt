package com.example.zubrilkaenglish.repositories.retrofit

import com.example.zubrilkaenglish.models.PropModel
import retrofit2.Response
import retrofit2.http.GET

interface PropApi {
    @GET("/properties/get_update_at")
    suspend fun getUpdateAt():Response<PropModel>
}