package com.example.zubrilkaenglish.repositories.retrofit

import com.example.zubrilkaenglish.models.Profile
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ProfileApi {
    @POST("/ze-auth/auth/registration")
    suspend fun registration(@Body profile: Profile): Response<Profile?>
}