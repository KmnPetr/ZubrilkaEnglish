package com.example.zubrilkaenglish.repositories

import com.example.zubrilkaenglish.models.Word
import com.example.zubrilkaenglish.models.WordCard
import com.example.zubrilkaenglish.repositories.retrofit.RetrofitService
import com.example.zubrilkaenglish.repositories.room.RoomService
import com.example.zubrilkaenglish.utils.SIM_FORM_DATE
import com.example.zubrilkaenglish.utils.StatProgress
import java.text.SimpleDateFormat
import java.util.Date

class Repository {
    private val retrofitService=RetrofitService()
    private val roomService=RoomService()

    /**
     * функция получит список Words из сети и положит его в БД из БД вернет новые данные
     */
    suspend fun getAllWords():List<Word>{

//TODO непонятная ошибка при обновлении даты на сервере и перезагрузке данных с сервера, скорее всего изза нарушения порядка айдишников

        if(checkDictionaryVersionOnServer()){
            val list=retrofitService.getAllWords()
            if (list != null) {
                println("получили список с инета. Его размер = "+list.size)
                roomService.deleteAllWords()
                println("удалили старый список из БД")
                roomService.insertListWords(list)
                println("положили список в БД")

                val newDictionaryVersion=retrofitService.getDictionaryVersion()
                roomService.insertNewDictionaryVersion(newDictionaryVersion)

            }else{
                println("список с сервера был null")
            }
        }
        return roomService.getAllWords()
    }

    /**
     * если на сервере имеется более свежая версия списков данных, метод вернет true или если в базе нет сведений о дате последней версии вернет true
     */
    private suspend fun checkDictionaryVersionOnServer():Boolean{
        val serverDicVers: String? =retrofitService.getDictionaryVersion()
        println("Dictionary version from server: $serverDicVers")
        val roomDicVers:String?=roomService.getDictionaryVersion()
        println("Dictionary version from room: $roomDicVers")

        if (serverDicVers==null){
            println("server Dictionary Version is null")
            return false
        }else if(roomDicVers==null){
            println("room Dictionary Version is null")
            return true
        }else {

            if(serverDicVers==roomDicVers){
                println("Версия словаря на сервере изменилась, загружаем новый список")
                return false
            }else{
                println("Старая версия актуальна, оставляем...")
                return true
            }

        }
    }


    /**
     * достанет из базы все карточки с прогрессом пользователя и рассортирует в мапу
     */
    suspend fun getMapMyCards(): Map<String, List<WordCard>> {
        val listWordsCards = roomService.getListWordsCards()
        val mapMyCards = mutableMapOf<String,ArrayList<WordCard>>()

        mapMyCards["активные"] = ArrayList()
        mapMyCards["спящие"] = ArrayList()
        mapMyCards["выученные"] = ArrayList()
        listWordsCards?.forEach {
            if (it.progressWord?.statProgress!=StatProgress.LEARNED.value&&compareDate(it.progressWord?.sleepTime)){
                mapMyCards["активные"]?.add(it)
            }else if (it.progressWord?.statProgress!=StatProgress.LEARNED.value&&!compareDate(it.progressWord?.sleepTime)){
                mapMyCards["спящие"]?.add(it)
            }else if(it.progressWord?.statProgress==StatProgress.LEARNED.value){
                mapMyCards["выученные"]?.add(it)
            }
        }
        return mapMyCards
    }

    /**
     * функция вернет false, если входящая в параметры дата еще не наступила
     */
    private fun compareDate(sleepTime: String?): Boolean{
        try {
            if (sleepTime==null){
                return true
            }else if(SimpleDateFormat(SIM_FORM_DATE).parse(sleepTime).before(Date())){
                return true
            }
        } catch (e: Exception) {
            return false
        }
        return false
    }


    /**
     * функция вернет список всех WordCard, в том числе и тех, у которых нет(null) ProgressWord
     * Запрос будет произведен к БД без попытки подключения к сети
     */
    suspend fun getAllWordCards(): List<WordCard> {
        return roomService.getAllWordCards()
    }
}