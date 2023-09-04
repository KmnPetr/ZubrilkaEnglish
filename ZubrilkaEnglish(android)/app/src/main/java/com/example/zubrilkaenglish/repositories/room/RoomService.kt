package com.example.zubrilkaenglish.repositories.room

import com.example.zubrilkaenglish.models.Word
import com.example.zubrilkaenglish.models.ProgressWord
import com.example.zubrilkaenglish.models.WordWithProgress

class RoomService{

    private val dataBase = DataBase.getInstanseDB()


    suspend fun insertListWords(listWords:List<Word>) {
            dataBase.getWordDAO().insertListWords(listWords)
    }

    suspend fun getAllWords(): List<Word> {
        return dataBase.getWordDAO().getAllWords()
    }

    suspend fun getWordById(id: Int): Word {
        return dataBase.getWordDAO().getWordById(id)
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

    /**
     * сохранит ProgressWord в БД
     * тем самым добавит новое слово/фразу в изучаемые
     */
    suspend fun addWordToTraining(progressWord: ProgressWord) {
        dataBase.getProgressDAO().insertProgressWord(progressWord)
    }

    suspend fun getAllWordsWithProgress(): List<WordWithProgress>? {
        return dataBase.getProgressDAO().getAllWordsWithProgress()
    }
}