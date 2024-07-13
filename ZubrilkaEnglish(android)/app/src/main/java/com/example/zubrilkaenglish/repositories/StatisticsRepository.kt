package com.example.zubrilkaenglish.repositories

import com.example.zubrilkaenglish.models.StatisticsDTO
import com.example.zubrilkaenglish.repositories.retrofit.RetrofitService
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

/**
 * репозиторий предоставляет и обрабатывает информацию по статистике пользователя
 */
class StatisticsRepository private constructor(){
    companion object{
        val instance:StatisticsRepository by lazy { StatisticsRepository() }
    }
    private val retrofitService = RetrofitService()
    private val profileRepository = ProfileRepository.instance
    val listStats: MutableStateFlow<ArrayList<StatisticsDTO>?> = MutableStateFlow(null)
    var ownStatsPosition: Int? = null
    var ownStats: StatisticsDTO? = null

    /**
     * запросит список статистики рейтинга первых 1500 пользователей набравших найбольшее количество очков
     * в списке будет присутствовать один обьект статистики пользователя этого устройства, или не будет его там
     * отсортирует
     * сам назначит заработанные места пользователям, так как сервер этим не занимается (бережет нагрузку)
     */
    fun getStatFirst1500() {
        GlobalScope.launch {
            val ownId:Long? = profileRepository.profile.value?.id
            val listStats: ArrayList<StatisticsDTO>? = retrofitService.getStatisticsApi().getStatFirst1500(ownId).body() as ArrayList<StatisticsDTO>?
            if (listStats != null) {

                listStats.sortByDescending { it.points }

                var offset:Int = 0
                listStats.forEachIndexed { index, stat->

                    if (stat.personId == ownId){ //если наткнулись на статистику текущего пользователя
                        ownStatsPosition = index
                        ownStats = stat
                    }

                    if (index!=0&& listStats[index].points == listStats[index-1].points) offset--

                    listStats[index].place = index + 1 + offset

                }
                this@StatisticsRepository.listStats.value = listStats
            }
        }
    }

    /**
     * очистиит лист статистики и сопутсвующих данных
     */
    fun clearStatList() {
        ownStatsPosition = null
        ownStats = null
        listStats.value?.clear()
        listStats.value = null
    }
}