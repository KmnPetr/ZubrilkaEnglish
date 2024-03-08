package com.example.ze_adminandroid.services

import com.example.ze_adminandroid.models.Word
import com.example.ze_adminandroid.services.room.DataBase
import kotlinx.coroutines.flow.Flow

class RoomService {

    private val dataBase = DataBase.getInstanceDB()
    suspend fun saveEditableWord(word: Word) {
        dataBase.getEditedWordDAO().saveEditableWord(word)
    }

    fun getFlowAllEditedWords(): Flow<List<Word>> {
        return dataBase.getEditedWordDAO().getAllEditedWords()
    }

}