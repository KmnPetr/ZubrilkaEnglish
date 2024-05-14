package com.example.zubrilkaenglish.repositories.retrofit

import com.example.zubrilkaenglish.models.Profile
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface ProfileApi {
    @POST("/ze-auth/auth/registration")
    suspend fun registration(@Body profile: Profile): Response<Profile?>
    @POST("/ze-auth/auth/login")
    @FormUrlEncoded
    suspend fun loginRequest(@Field("username") username: String, @Field("password") password: String): Response<Profile?>
}