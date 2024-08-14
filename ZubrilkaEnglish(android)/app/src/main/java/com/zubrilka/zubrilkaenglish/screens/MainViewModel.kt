package com.zubrilka.zubrilkaenglish.screens

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zubrilka.zubrilkaenglish.models.Profile
import com.zubrilka.zubrilkaenglish.models.WordCard
import com.zubrilka.zubrilkaenglish.repositories.CardsRepository
import com.zubrilka.zubrilkaenglish.repositories.ProfileRepository
import com.zubrilka.zubrilkaenglish.repositories.VoiceRepository
import com.zubrilka.zubrilkaenglish.utils.LOG
import kotlinx.coroutines.launch

class MainViewModel: ViewModel() {

    private val cardsRepository = CardsRepository.instance
    private val voiceRepository = VoiceRepository.instance
    private val profileRepository = ProfileRepository.instance


    val listAllWords: MutableLiveData<List<WordCard>> = MutableLiveData()
    val mapWordsByTopic: MutableLiveData<Map<String, ArrayList<WordCard>>> = MutableLiveData()
    val namesTopics: MutableLiveData<List<String>> = MutableLiveData()

    val profile: MutableLiveData<Profile?> = MutableLiveData()

    init {
        viewModelScope.launch{
            profileRepository.profile.collect{
                Log.d(LOG,"Profile was changed: ViewModel: "+it?.toString())
                this@MainViewModel.profile.postValue(it)
            }
        }

        getListWordsFromRepository()

    }

    /**
     * Получает список Words из репозитория и заполняет ими поля listAllWords, mapWordsByTopic, namesTopics
     */
    private fun getListWordsFromRepository() {
        viewModelScope.launch {
            //этот вызов нельзя убирать, данные с сервера не подгрузятся
            cardsRepository.getAllWordsFromServerOrDB()

            listAllWords.value = cardsRepository.getAllWordCards()

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

        var topicsName = mapWords.keys.toList()

        return topicsName
    }
}