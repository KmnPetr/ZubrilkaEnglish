package com.example.ze_adminandroid.screens.catalogWords

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ze_adminandroid.models.Word
import com.example.ze_adminandroid.repositories.WordRepository
import kotlinx.coroutines.launch

class CatalogWordsViewModel : ViewModel() {
    val wordRepository: WordRepository = WordRepository.instance


    //в списке находится информация, изменялся ли в показываемом на данный момент фрагменте список папок на список слов содержащихся в папках
    val isRecyclerChanged: MutableLiveData<ArrayList<Boolean>> = MutableLiveData()

    val mapWordsByTopic: MutableLiveData<Map<String, List<Word>>> = MutableLiveData()
    val namesTopics: MutableLiveData<List<String>> = MutableLiveData()


    init {
        dataDownload()
    }

    /**
     * загрузит\обновит данные из репозитория
     */
    private fun dataDownload(){
        viewModelScope.launch {
            val listAllWords = wordRepository.getAllWords()
            mapWordsByTopic.value = sortWordsByTopic(listAllWords)
            namesTopics.value = fillNamesTopics(mapWordsByTopic.value as MutableMap<String, ArrayList<Word>>)

//            mapUserCards.value = repository.getMapMyCards()
//            namesTopicsUserCards.value = mapUserCards.value?.keys?.toList()
        }
    }

    /**
     * функция отсортирует массив элементов Word по темам/группам
     */
    private fun sortWordsByTopic(listWords: List<Word>): MutableMap<String, ArrayList<Word>> {
        val mapWords = mutableMapOf<String,ArrayList<Word>>()

        mapWords["Все слова"] = listWords as ArrayList<Word>

        listWords.forEach { word ->
            if (mapWords[word.topic] == null) {
                mapWords[word.topic] = ArrayList()
            }
            mapWords[word.topic]?.add(word)
        }

        return mapWords
    }

    /**
     * функция создаст список тем/названий групп слов из ключей mapWordsByTopic
     */
    private fun fillNamesTopics(mapWords: Map<String, List<Word>>): List<String> {
        return mapWords.keys.toList()
    }
}