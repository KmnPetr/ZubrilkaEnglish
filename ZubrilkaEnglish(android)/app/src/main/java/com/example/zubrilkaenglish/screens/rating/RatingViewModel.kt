package com.example.zubrilkaenglish.screens.rating

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.zubrilkaenglish.models.StatisticsDTO
import com.example.zubrilkaenglish.repositories.StatisticsRepository
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException
import kotlin.random.Random

class RatingViewModel : ViewModel() {
    private val statisticsRepository = StatisticsRepository.instance

    val statistics: MutableLiveData<ArrayList<StatisticsDTO>?> = MutableLiveData(null)
//    val testStatisticsDTO:StatisticsDTO = StatisticsDTO("TestUser",null,111,9500, "dd-mm-dd",0)
    var ownPosition: Int? = null//позиция в списке карточки основного пользователя
    var ownStats: StatisticsDTO? = null

    init {
        viewModelScope.launch {
            statisticsRepository.listStats.collect {
                ownStats = statisticsRepository.ownStats
                ownPosition = statisticsRepository.ownStatsPosition
                statistics.value = it
            }
        }
    }
}