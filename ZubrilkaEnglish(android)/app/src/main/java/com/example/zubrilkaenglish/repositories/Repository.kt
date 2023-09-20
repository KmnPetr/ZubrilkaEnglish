package com.example.zubrilkaenglish.repositories

import android.widget.Toast
import com.example.zubrilkaenglish.models.ProgressWord
import com.example.zubrilkaenglish.models.Word
import com.example.zubrilkaenglish.models.WordCard
import com.example.zubrilkaenglish.repositories.retrofit.RetrofitService
import com.example.zubrilkaenglish.repositories.room.RoomService
import com.example.zubrilkaenglish.utils.MyApplication
import com.example.zubrilkaenglish.utils.SIM_FORM_DATE
import com.example.zubrilkaenglish.utils.StatProgress
import org.threeten.bp.ZonedDateTime
import org.threeten.bp.format.DateTimeFormatter
import java.text.SimpleDateFormat
import java.util.Date

class Repository {
    private val retrofitService=RetrofitService()
    private val roomService=RoomService()

    /**
     * функция получит список Words из сети и положит его в БД из БД вернет новые данные
     */
    suspend fun getAllWords():List<Word>{

        if(checkUpdateAtOnServer()){
            val list=retrofitService.getAllWords()
            if (list != null) {
                println("получили список с инета. Его размер = "+list.size)
                roomService.deleteAllWords()
                println("удалили старый список из БД")
                roomService.insertListWords(list)
                println("положили список в БД")

                val newUpdateAt=retrofitService.getUpdateAt()
                roomService.insertNewUpdatedAt(newUpdateAt)

            }else{
                println("список с сервера был null")
            }
        }
        return roomService.getAllWords()
    }

    /**
     * функция получит Word из БД по id
     */
    suspend fun getWordByIdFromDB(id:Int):Word{
        return roomService.getWordById(id)
    }

    /**
     * если на сервере имеется более свежая версия списков данных, метод вернет true или если в базе нет сведений о дате последней версии вернет true
     */
    private suspend fun checkUpdateAtOnServer():Boolean{
        val serverUpAt: String? =retrofitService.getUpdateAt()
        println(serverUpAt)
        val roomUpAt:String?=roomService.getUpdatedAt()
        println(roomUpAt)

        if (serverUpAt==null){
            println("serverUpAt is null")
            return false
        }else if(roomUpAt==null){
            println("roomUpAt is null")
            return true
        }else {
            val parsedServUpAt= ZonedDateTime.parse(serverUpAt, DateTimeFormatter.ISO_ZONED_DATE_TIME)
            val parsedRoomUpAt= ZonedDateTime.parse(roomUpAt, DateTimeFormatter.ISO_ZONED_DATE_TIME)

            if(parsedServUpAt.isAfter(parsedRoomUpAt)){
                println("parsedServUpAt is after parsedRoomUpAt")
                return true
            }else {
                println("parsedServUpAt is before or is equals parsedRoomUpAt")
                return false
            }
        }
    }

    /**
     * сохранит ProgressWord в БД
     * тем самым добавит новое слово/фразу в изучаемые
     */
    suspend fun addWordToTraining(idWord: Int?) {
        try {
            val progressWord = ProgressWord(null,idWord!!,0,StatProgress.NEW.value,SimpleDateFormat(SIM_FORM_DATE).format(Date()))
            roomService.addWordToTraining(progressWord)
            Toast.makeText(MyApplication.context,"Слово/фраза добавлено(а) в изучаемые.",Toast.LENGTH_LONG).show()
        }catch (e: Exception){
            Toast.makeText(MyApplication.context,e.javaClass.name+"\n"+e.message,Toast.LENGTH_LONG).show()
        }
    }

    suspend fun getListWordsCards(): List<WordCard>? {
        return roomService.getListWordsCards()
    }

    /**
     * обновит ProgressWord
     */
    suspend fun updateProgressWord(progressWord: ProgressWord) {
        roomService.updateProgressWord(progressWord)
    }

    /**
     * найдет ProgressWord по id
     */
    suspend fun getProgressWordById(progId: Int?): ProgressWord? {
        return roomService.getProgressWordById(progId)
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
     * функция достанет Word из БД в виде WordCard вместе с прогрессом пользователя по этой карточке
     */
    suspend fun getWordCardById(wordId: Int?): WordCard {
        return roomService.getWordCardById(wordId)
    }
}