package com.example.zubrilkaenglish.screens.activity

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.zubrilkaenglish.repositories.Repository
import com.example.zubrilkaenglish.models.Word
import com.example.zubrilkaenglish.models.WordCard
import com.example.zubrilkaenglish.repositories.CardsRepository
import kotlinx.coroutines.launch

class MainViewModel: ViewModel() {

    private val repository = Repository()
    //инициализируем синглетон в первый раз
    private val cardsRepository = CardsRepository.instance

    val listAllWords: MutableLiveData<List<WordCard>> = MutableLiveData()
    val mapWordsByTopic: MutableLiveData<Map<String, ArrayList<WordCard>>> = MutableLiveData()
    val namesTopics: MutableLiveData<List<String>> = MutableLiveData()

    init {
        getListWordsFromRepository()
    }

    /**
     * Получает список Words из репозитория и заполняет ими поля listAllWords, mapWordsByTopic, namesTopics
     */
    private fun getListWordsFromRepository() {
        viewModelScope.launch {
            //этот вызов нельзя убирать, данные с сервера не подгрузятся
            repository.getAllWords()

            listAllWords.value = repository.getAllWordCards()

            //заполняем mapWordsByTopic
            mapWordsByTopic.value = listAllWords.value?.let { sortWordsByTopic(it) }

            namesTopics.value = mapWordsByTopic.value?.let { fillNamesTopics(it) }
        }
    }


    /**
     * функция отсортирует массив элементов Word по темам/группам
     */
    fun sortWordsByTopic(listWords: List<WordCard>): MutableMap<String, ArrayList<WordCard>> {
        val mapWords = mutableMapOf<String,ArrayList<WordCard>>()

        mapWords["Все слова"] = listWords as ArrayList<WordCard>

        listWords.forEach { wordCard ->
            if (mapWords[wordCard.word.groupWord] == null) {
                mapWords[wordCard.word.groupWord] = ArrayList()
            }
            mapWords[wordCard.word.groupWord]?.add(wordCard)
        }

        return mapWords
    }

    /**
     * функция создаст список тем/названий групп слов из ключей mapWordsByTopic
     */
    private fun fillNamesTopics(mapWords: Map<String, List<WordCard>>): List<String> {

        var topicsName = mapWords.keys.toList()

        return topicsName
    }
}