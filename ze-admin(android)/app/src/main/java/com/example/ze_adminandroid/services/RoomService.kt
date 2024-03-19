package com.example.ze_adminandroid.services

import androidx.room.Query
import com.example.ze_adminandroid.models.PropModel
import com.example.ze_adminandroid.models.Voice
import com.example.ze_adminandroid.models.Word
import com.example.ze_adminandroid.services.room.DataBase
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class RoomService {

    private val dataBase = DataBase.getInstanceDB()
    suspend fun saveEditableWord(word: Word) {
        dataBase.getEditedWordDAO().saveEditableWord(word)
    }

    fun getFlowAllEditedWords(): Flow<List<Word>> {
        return dataBase.getEditedWordDAO().getAllEditedWords()
    }

    suspend fun saveNewVoice(voice: Voice) {
        dataBase.getCreatedVoiceDAO().saveVoice(voice)
    }

    /**
     * установит новое значение в таблице prop_table по ключу last-host
     */
    fun insertNewLastHost(host: String){
        GlobalScope.launch {
            dataBase.getPropDAO().insertNewLastHost(PropModel("last-host",host))
        }
    }

    /**
     * выдаст значение из таблицы prop_table по ключу last-host
     */
    suspend fun getLastHost():String?{
        return dataBase.getPropDAO().getLastHost()?.value
    }


    suspend fun getVoiceByName(name: String): Voice?{
        return dataBase.getCreatedVoiceDAO().getVoiceByName(name)
    }

    /**
     * вернет все Words с полем isReady = fulse
     */
    suspend fun getNotReadyWords():List<Word>?{
        return dataBase.getEditedWordDAO().getNotReadyWords(false)
    }

    //вернет количество сущностей  Voice из БД
    fun getCountVoices(): Flow<Int> {
        return dataBase.getCreatedVoiceDAO().getCount()
    }
}