package com.example.zubrilkaenglish.screens.catalogCards

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.zubrilkaenglish.models.WordCard
import com.example.zubrilkaenglish.repositories.Repository
import com.example.zubrilkaenglish.utils.SearchObject
import kotlinx.coroutines.launch

class CatalogCardsViewModel : ViewModel() {

    private val repository = Repository()

    var searchCreated: Boolean = false //указывает, создан ли ранее фрагмент поиска слова
    var lastPositionTablayout: Int = 0 //указ.последний используемый фрагмент для возврата на него после удаления поискового фрагмента
    val listSearchWords: MutableLiveData<List<WordCard>> = MutableLiveData()

    val mapWordsByTopic: MutableLiveData<Map<String, ArrayList<WordCard>>> = MutableLiveData()
    val namesTopics: MutableLiveData<List<String>> = MutableLiveData()

    val mapUserCards: MutableLiveData<Map<String, List<WordCard>>> = MutableLiveData()
    val namesTopicsUserCards: MutableLiveData<List<String>> = MutableLiveData()

    init {
        //заполняем некоторые переменные данными
        viewModelScope.launch {
            val listAllCards = repository.getAllWordCards()
            mapWordsByTopic.value = sortWordsByTopic(listAllCards)
            namesTopics.value = fillNamesTopics(mapWordsByTopic.value as MutableMap<String, ArrayList<WordCard>>)

            mapUserCards.value = repository.getMapMyCards()
            namesTopicsUserCards.value = mapUserCards.value?.keys?.toList()
        }
    }


    /**
     * будет изменять список при вводе в поисковую строку нового слова
     */
    fun changeListSearchWord(word: String){
        listSearchWords.value = SearchObject.instance.search(word)
    }

    /**
     * функция отсортирует массив элементов Word по темам/группам
     */
    private fun sortWordsByTopic(listWords: List<WordCard>): MutableMap<String, ArrayList<WordCard>> {
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