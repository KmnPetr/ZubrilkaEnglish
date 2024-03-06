package com.example.ze_adminandroid.repositories

import com.example.ze_adminandroid.models.Word
import com.example.ze_adminandroid.services.RetrofitService
import com.example.ze_adminandroid.services.RoomService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.time.delay
import java.time.Duration

class WordRepository private constructor(){

    companion object{
        val instance: WordRepository by lazy { WordRepository() }
    }

    private val retrofitService = RetrofitService.instance
    private var roomService: RoomService = RoomService()
    private var listWords: List<Word> = emptyList()

    init {
        GlobalScope.launch(Dispatchers.Default) {
            val list: List<Word>? = retrofitService.getAllWordsFromNetwork()
            if (list != null) {
                println("ПОЛУЧЕН СПИСОК С СЕТИ. Размер: "+list.size)
                listWords = list
            }
        }
    }


    fun getAllWords(): List<Word> {
        while (listWords.isEmpty()){
            Thread.sleep(100)
        }
        return listWords
    }

    fun saveEditableWord(word: Word) {
        GlobalScope.launch(Dispatchers.Default) {
            roomService.saveEditableWord(word)
            println("size: "+roomService.getAllEditedWords().size)
        }
    }

}