package com.example.ze_adminandroid.services

import com.example.ze_adminandroid.models.Word
import com.example.ze_adminandroid.services.room.DataBase

class RoomService {

    private val dataBase = DataBase.getInstanceDB()
    suspend fun saveEditableWord(word: Word) {
        dataBase.getEditedWordDAO().saveEditableWord(word)
    }

//    suspend fun getAllEditedWords(): List<Word> {
//        return dataBase.getEditedWordDAO().getAllEditedWords()
//    }

}