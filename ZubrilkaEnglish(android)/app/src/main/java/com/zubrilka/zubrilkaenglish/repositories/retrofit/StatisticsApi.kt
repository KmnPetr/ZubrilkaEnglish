package com.zubrilka.zubrilkaenglish.repositories.retrofit

import com.zubrilka.zubrilkaenglish.models.StatisticsDTO
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface StatisticsApi {
    /**
     * выдаст список статистики рейтинга первых 1500 пользователей набравших найбольшее количество очков
     * в списке будет присутствовать один обьект статистики пользователя этого устройства, или не будет его там
     * список прийдет неотсортированным,
     * завоеванные места необходимо проставить на фронте так как сервер этим не занимается (бережет нагрузку)
     */
    @GET("/stat/first1500users_rating")
    suspend fun getStatFirst1500(@Query("ownId") ownId: Long?): Response<List<StatisticsDTO>>

    /**
     * отправит на сервер количество очков заработанных в офлайн режимах тренировки
     */
    @POST("/stat/save_offline_points")
    suspend fun sendOfflinePoints(
        @Header("Authorization") accessToken: String?,
        @Query("offlinePoints") offlinePoints: Int
    ): Response<Any?>
}