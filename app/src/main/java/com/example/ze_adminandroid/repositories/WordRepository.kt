package com.example.ze_adminandroid.repositories

import com.example.ze_adminandroid.models.Word
import com.example.ze_adminandroid.services.RetrofitService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class WordRepository private constructor(){

    companion object{
        val instance: WordRepository by lazy { WordRepository() }
    }

    private val retrofitService = RetrofitService.instance
    private val mapWords: Map<Int, Word> = HashMap()

    init {
        GlobalScope.launch(Dispatchers.Default) {
            val listWords: List<Word>? = retrofitService.getAllWordsFromNetwork()
            if (listWords != null) {
                println("ПОЛУЧЕН СПИСОК С СЕТИ. Размер: "+listWords.size)
            }
        }
    }
}