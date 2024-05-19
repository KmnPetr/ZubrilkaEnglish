package com.example.zubrilkaenglish.repositories.retrofit

import com.example.zubrilkaenglish.models.Profile
import com.example.zubrilkaenglish.models.PropModel
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

interface ProfileApi {
    @POST("/ze-auth/auth/registration")
    suspend fun registration(@Body profile: Profile): Response<Profile?>
    @POST("/ze-auth/auth/login")
    @FormUrlEncoded
    suspend fun loginRequest(@Field("username") username: String, @Field("password") password: String): Response<Profile?>

    @PATCH("/ze-auth/profile/update-field/{id}")
    suspend fun changeName(@Path("id") userId: Long,
                           @Header("Authorization") accessToken: String,
                           @Body propWithNewName: PropModel): Response<Profile?>

    @GET("/ze-auth/auth/getAccessToken")
    suspend fun refreshAccessToken(
        @Header("Authorization") refreshToken: String
    ): Response<PropModel?>
}