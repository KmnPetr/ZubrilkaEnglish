package com.example.ze_adminandroid.repositories

import com.example.ze_adminandroid.models.Word
import com.example.ze_adminandroid.services.RetrofitService
import com.example.ze_adminandroid.services.RoomService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class WordRepository private constructor(){


    companion object{
        val instance: WordRepository by lazy { WordRepository() }
    }

    private val retrofitService = RetrofitService.instance
    private var roomService: RoomService = RoomService()
    private val listWords: MutableStateFlow<List<Word>> = MutableStateFlow(arrayListOf())

    init {
        GlobalScope.launch(Dispatchers.Default) {
            val list: List<Word>? = retrofitService.getAllWordsFromNetwork()
            if (list != null) {
                println("ПОЛУЧЕН СПИСОК С СЕТИ. Размер: "+list.size)
                listWords.value = list
            }
        }
    }


    fun getAllWords(): Flow<List<Word>> {
        return listWords
    }

    fun saveEditableWord(word: Word) {
        GlobalScope.launch(Dispatchers.Default) {
            roomService.saveEditableWord(word)
        }
    }

    fun getFlowAllEditedWords():Flow<List<Word>> = roomService.getFlowAllEditedWords()
}