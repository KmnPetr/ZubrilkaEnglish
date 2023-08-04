package com.example.zubrilkaenglish.repositories.room

import android.util.Log
import android.widget.Toast
import com.example.zubrilkaenglish.models.PropModel
import com.example.zubrilkaenglish.models.Word
import com.example.zubrilkaenglish.utils.MyApplication

class RoomService{

    private val dataBase=DataBase.getInstanseDB()


    suspend fun insertListWords(listWords:List<Word>) {
            dataBase.getWordDAO().insertListWords(listWords)
    }

    suspend fun getAllWords(): List<Word> {
        return dataBase.getWordDAO().getAllWords()
    }

    suspend fun deleteAllWords(){
        dataBase.getWordDAO().deleteAllWords()
    }

    suspend fun getUpdatedAt(): String? {
            return dataBase.getPropDAO().getUpdatedAt()?.value
    }

    /**
     * функция добавит новое значение по ключу update_at или обновит старое
     */
    suspend fun insertNewUpdatedAt(newUpdatedAt: String?) {
        if(dataBase.getPropDAO().getUpdatedAt()!=null){
            dataBase.getPropDAO().updateUpdatedAt(newUpdatedAt)
        }else dataBase.getPropDAO().insertNewUpdatedAt(newUpdatedAt)
    }
}