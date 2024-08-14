package com.zubrilka.zubrilkaenglish.repositories.room

import com.zubrilka.zubrilkaenglish.models.Word
import com.zubrilka.zubrilkaenglish.models.ProgressWord
import com.zubrilka.zubrilkaenglish.models.Voice
import com.zubrilka.zubrilkaenglish.models.WordCard

class RoomService{

    private val dataBase = DataBase.getInstanceDB()

    fun getMemoDAO() = dataBase.getMemoDAO() //вернет MemoDAO с его методами
    fun getPropDAO() = dataBase.getPropDAO() //вернет PropDAO с его методами
    fun getProgressDAO() = dataBase.getProgressDAO() //даст прямой доступ к методам ProgressDAO
    fun getWordDAO() = dataBase.getWordDAO() //даст прямой доступ к методам WordDAO

    suspend fun getVoiceByName(name: String): Voice?{
        return dataBase.getVoiceDAO().getVoiceByName(name)
    }
    suspend fun insertListWords(listWords:List<Word>) {
        println("method \"insertListWords\" called")
            dataBase.getWordDAO().insertListWords(listWords)
    }

    suspend fun getAllWords(): List<Word> {
        println("method \"room getAllWords\" called")
        return dataBase.getWordDAO().getAllWords()
    }


    suspend fun getDictionaryVersion(): String? {
            return dataBase.getPropDAO().getDictionaryVersion()?.value
    }

    /**
     * функция добавит новое значение по ключу update_at или обновит старое
     */
    suspend fun insertNewDictionaryVersion(newDictionaryVersion: String?) {
        if(dataBase.getPropDAO().getDictionaryVersion()!=null){
            dataBase.getPropDAO().updateDictionaryVersion(newDictionaryVersion)
        }else dataBase.getPropDAO().insertNewDictionaryVersion(newDictionaryVersion)
    }

    /**
     * сохранит ProgressWord в БД
     * тем самым добавит новое слово/фразу в изучаемые
     */
    suspend fun addWordToTraining(progressWord: ProgressWord) {
        dataBase.getProgressDAO().insertProgressWord(progressWord)
    }

    suspend fun getListWordsCards(): List<WordCard>? {
        return dataBase.getProgressDAO().getListWordsCards()
    }

    suspend fun updateProgressWord(progressWord: ProgressWord) {
        dataBase.getProgressDAO().updateProgressWord(progressWord)
    }

    suspend fun getProgressWordById(progId: Int?): ProgressWord? {
        return dataBase.getProgressDAO().getProgressWordById(progId)
    }

    /**
     * функция достанет Word из БД в виде WordCard вместе с прогрессом пользователя по этой карточке
     */
    suspend fun getWordCardById(wordId: Int?): WordCard {
        return dataBase.getWordDAO().getWordCardById(wordId)
    }

    suspend fun deleteProgressByWordId(idWord: Int?) {
        dataBase.getProgressDAO().deleteProgressByWordId(idWord)
    }

    /**
     * функция вернет список всех WordCard, в том числе и тех, у которых нет(null) ProgressWord
     */
    suspend fun getAllWordCards(): List<WordCard> {
        return dataBase.getWordDAO().getAllWordCards()
    }

    suspend fun insertNewVoice(voice: Voice) {
        if (voice.voiceData != null){
            dataBase.getVoiceDAO().insertNewVoice(voice)
        }
    }



}