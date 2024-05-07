package com.example.zubrilkaenglish.screens.catalogCards

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.zubrilkaenglish.models.WordCard
import com.example.zubrilkaenglish.repositories.CardsRepository
import com.example.zubrilkaenglish.utils.SearchObject
import kotlinx.coroutines.launch

class CatalogCardsViewModel : ViewModel() {

    private val cardsRepository = CardsRepository.instance

    var searchCreated: Boolean = false //указывает, создан ли ранее фрагмент поиска слова
    var lastPositionTablayout: Int = 0 //указ.последний используемый фрагмент для возврата на него после удаления поискового фрагмента
    val listSearchWords: MutableLiveData<List<WordCard>> = MutableLiveData()

    val mapWordsByTopic: MutableLiveData<Map<String, List<WordCard>>> = MutableLiveData()
    val namesTopics: MutableLiveData<List<String>> = MutableLiveData()

    val mapUserCards: MutableLiveData<Map<String, List<WordCard>>> = MutableLiveData()
    val namesTopicsUserCards: MutableLiveData<List<String>> = MutableLiveData()

    //в списке находится информация, изменялся ли в показываемом на данный момент фрагменте список папок на список слов содержащихся в папках
    val isRecyclerChanged: MutableLiveData<ArrayList<Boolean>> = MutableLiveData()

    init {
        dataDownload()
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
            if (mapWords[wordCard.word.topic] == null) {
                mapWords[wordCard.word.topic] = ArrayList()
            }
            mapWords[wordCard.word.topic]?.add(wordCard)
        }

        return mapWords
    }

    /**
     * функция создаст список тем/названий групп слов из ключей mapWordsByTopic
     */
    private fun fillNamesTopics(mapWords: Map<String, List<WordCard>>): List<String> {
        val topicsName = mapWords.keys.toList()
        return topicsName
    }

    /**
     * загрузит\обновит данные из репозитория
     */
    private fun dataDownload(){
        viewModelScope.launch {
            val listAllCards = cardsRepository.getAllWordCards()
            mapWordsByTopic.value = sortWordsByTopic(listAllCards)
            namesTopics.value = fillNamesTopics(mapWordsByTopic.value as MutableMap<String, ArrayList<WordCard>>)

            mapUserCards.value = cardsRepository.getMapMyCards()
            namesTopicsUserCards.value = mapUserCards.value?.keys?.toList()
        }
    }
}