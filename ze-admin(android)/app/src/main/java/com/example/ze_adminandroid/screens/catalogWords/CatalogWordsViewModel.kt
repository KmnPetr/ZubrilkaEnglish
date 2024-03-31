package com.example.ze_adminandroid.screens.catalogWords

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ze_adminandroid.models.Word
import com.example.ze_adminandroid.repositories.WordRepository
import com.example.ze_adminandroid.utils.SearchObject
import kotlinx.coroutines.launch

class CatalogWordsViewModel : ViewModel() {
    val wordRepository: WordRepository = WordRepository.instance


    //в списке находится информация, изменялся ли в показываемом на данный момент фрагменте список папок на список слов содержащихся в папках
    val isRecyclerChanged: MutableLiveData<ArrayList<Boolean>> = MutableLiveData()
    var lastPositionTablayout: Int = 0 //указ.последний используемый фрагмент для возврата на него после удаления поискового фрагмента
    val listSearchWords: MutableLiveData<List<Word>> = MutableLiveData()
    var searchCreated: Boolean = false //указывает, создан ли ранее фрагмент поиска слова

    val mapWordsByTopic: MutableLiveData<Map<String, List<Word>>> = MutableLiveData()
    val namesTopics: MutableLiveData<List<String>> = MutableLiveData()

    //список измененных и новых созданных слов из БД
    val mapEditedWords: MutableLiveData<Map<String, List<Word>>> = MutableLiveData()
    val namesEditedTopics: MutableLiveData<List<String>> = MutableLiveData()

    //неготовое с прошлой сессии приложения Word
    val notReadyWord: MutableLiveData<Word?> = MutableLiveData(null)


    init {

        //список слов словаря из сети подгружает Flow из репозитория
        viewModelScope.launch {
            wordRepository.filteredListAllWords/*.getAllWords()*/.collect { word ->
                mapWordsByTopic.value = sortWordsByTopic(word)
                namesTopics.value = fillNamesTopics(mapWordsByTopic.value as MutableMap<String, ArrayList<Word>>)
            }
        }

        //список измененных слов подгружает Flow при любом изменении в БД
        viewModelScope.launch {
            wordRepository.getFlowAllEditedWords().collect { words ->
                mapEditedWords.value = sortWordsByTopic(words)
                namesEditedTopics.value = fillNamesTopics(mapEditedWords.value as MutableMap<String, ArrayList<Word>>)
            }
        }

        checkNotReadyWord()
    }


    /**
     * если в БД имелось незавершеное недавно слово
     * перенаправит на фрагмент для завершения его редактирования
     */
    private fun checkNotReadyWord() {
        viewModelScope.launch {
            wordRepository.notReadyWord.collect{
                if (it!=null) notReadyWord.value = it
            }
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



    /**
     * будет изменять список при вводе в поисковую строку нового слова
     */
    fun changeListSearchWord(word: String){
        listSearchWords.value = SearchObject.instance.search(word)
    }
}