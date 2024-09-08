package com.zubrilka.zubrilkaenglish.repositories.retrofit

import com.zubrilka.zubrilkaenglish.models.Profile
import com.zubrilka.zubrilkaenglish.models.PropModel
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST

interface ProfileApi {
    @POST("/profile/change-password")
    suspend fun changePassword(
        @Header("Authorization") accessToken: String,
        @Body params: Map<String,String>): Response<Profile?>
    @POST("/auth/registration")
    suspend fun registration(@Body profile: Profile): Response<Profile?>
    @POST("/auth/login")
    @FormUrlEncoded
    suspend fun loginRequest(@Field("username") username: String, @Field("password") password: String): Response<Profile?>

    @PATCH("/profile/update-field")
    suspend fun changeName(@Header("Authorization") accessToken: String,
                           @Body propWithNewName: PropModel): Response<Profile?>

    @GET("/auth/getAccessToken")
    suspend fun refreshAccessToken(
        @Header("Authorization") refreshToken: String
    ): Response<PropModel?>

    /**
     * запросит временный профиль
     */
    @GET("/profile/getTemporaryProfile")
    suspend fun requestTemporaryProfile(): Response<Profile?>
}