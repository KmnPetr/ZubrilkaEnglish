package com.example.zubrilkaenglish.screens.rating

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.zubrilkaenglish.models.StatisticsDTO
import java.lang.IllegalArgumentException
import kotlin.random.Random

class RatingViewModel : ViewModel() {

    val statistics: MutableLiveData<List<StatisticsDTO>?> = MutableLiveData(null)
    val testStatisticsDTO:StatisticsDTO = StatisticsDTO("TestUser",null,111,9500, "dd-mm-dd",0)
    var ownPosition:Int? = null //позиция в списке карточки основного пользователя

    init {
        generateTestList(1500,testStatisticsDTO)
    }

    /**
     * создаст временный тестовый лист
     */
    private fun generateTestList(size: Int, statisticsDTO: StatisticsDTO) {
        if (statisticsDTO.personId==null&&statisticsDTO.personId>(size-1)) throw IllegalArgumentException("Исправь personId пожалуйста")

        var listStatistics:ArrayList<StatisticsDTO> = ArrayList(size)

        for (i in 0..size){
            if (i.toLong() ==testStatisticsDTO.personId){
                listStatistics.add(testStatisticsDTO)
                continue
            }
            listStatistics.add(StatisticsDTO(
                "Guest_"+i,
                null,
                i.toLong(),
                Random.nextInt(0, 10001).toLong(),
                "dd-mm-dd",
                Random.nextInt(-1000, 1001)))


        }

        listStatistics.sortByDescending { it.points }
        //проверка offset
        listStatistics.get(100).points = listStatistics.get(99).points
        listStatistics.get(101).points = listStatistics.get(100).points

        var offset:Int = 0
        listStatistics.forEachIndexed { index, stat ->
            if (index!=0&&stat.points==listStatistics.get(index-1).points) offset--
            stat.place = index + 1 + offset

            if (stat.personId == testStatisticsDTO.personId){
                ownPosition = index //запомним положение основного пользователя
            }
        }

        println("LIST SIZE: ${listStatistics.size}")
        statistics.value = listStatistics
    }
}