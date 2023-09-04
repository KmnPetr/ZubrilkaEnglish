package com.example.zubrilkaenglish.screens.activity

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.zubrilkaenglish.repositories.Repository
import com.example.zubrilkaenglish.models.Word
import kotlinx.coroutines.launch

class MainViewModel: ViewModel() {

    private val repository = Repository()

    val listAllWords: MutableLiveData<List<Word>> = MutableLiveData()
    val mapWordsByTopic: MutableLiveData<Map<String, ArrayList<Word>>> = MutableLiveData()
    val namesTopics: MutableLiveData<List<String>> = MutableLiveData()

    init {
        getListWordsFromRepository()
    }

    /**
     * Получает список Words из репозитория и заполняет ими поля listAllWords, mapWordsByTopic, namesTopics
     */
    private fun getListWordsFromRepository() {
        viewModelScope.launch {
            listAllWords.value=repository.getAllWords()

            //заполняем mapWordsByTopic
            mapWordsByTopic.value = listAllWords.value?.let { sortWordsByTopic(it) }

            namesTopics.value = mapWordsByTopic.value?.let { fillNamesTopics(it) }
        }
    }


    /**
     * функция отсортирует массив элементов Word по темам/группам
     */
    fun sortWordsByTopic(listWords: List<Word>): MutableMap<String, ArrayList<Word>> {
        val mapWords = mutableMapOf<String,ArrayList<Word>>()

        mapWords["Все слова"] = listWords as ArrayList<Word>

        listWords.forEach { word ->
            if (mapWords[word.groupWord] == null) {
                mapWords[word.groupWord] = ArrayList()
            }
            mapWords[word.groupWord]?.add(word)
        }

        return mapWords
    }

    /**
     * функция создаст список тем/названий групп слов из ключей mapWordsByTopic
     */
    private fun fillNamesTopics(mapWords: Map<String, List<Word>>): List<String> {

        var topicsName = mapWords.keys.toList()

        return topicsName
    }
}