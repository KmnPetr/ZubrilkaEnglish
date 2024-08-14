package com.zubrilka.zubrilkaenglish.repositories.retrofit

import com.zubrilka.zubrilkaenglish.models.PropModel
import retrofit2.Response
import retrofit2.http.GET

interface PropApi {
    @GET("/properties/get_dictionary_version")
    suspend fun getDictionaryVersion():Response<PropModel>
}