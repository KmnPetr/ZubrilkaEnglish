package com.example.zubrilkaenglish.screens.catalogCards

import com.example.zubrilkaenglish.utils.StatProgress
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.zubrilkaenglish.models.WordCard
import com.example.zubrilkaenglish.repositories.CardsRepository
import com.example.zubrilkaenglish.repositories.PropRepository
import com.example.zubrilkaenglish.repositories.room.PropKey
import com.example.zubrilkaenglish.utils.SearchObject
import kotlinx.coroutines.launch

class CatalogCardsViewModel : ViewModel() {

    private val cardsRepository = CardsRepository.instance
    private val propRepository = PropRepository.instance
    private val searchObject = SearchObject.instance

    var searchCreated: Boolean = false //указывает, создан ли ранее фрагмент поиска слова
    var lastPositionTablayout: Int = 0 //указ.последний используемый фрагмент для возврата на него после удаления поискового фрагмента
    val listSearchWords: MutableLiveData<List<WordCard>> = MutableLiveData()

    val mapWordsByTopic: MutableLiveData<Map<String, List<WordCard>>> = MutableLiveData()
    val namesTopics: MutableLiveData<List<String>> = MutableLiveData()

    val mapUserCards: MutableLiveData<Map<String, List<WordCard>>> = MutableLiveData()
    val namesTopicsUserCards: MutableLiveData<List<String>> = MutableLiveData()

    //в списке находится информация, изменялся ли в показываемом на данный момент фрагменте список папок на список слов содержащихся в папках
    val isRecyclerChanged: MutableLiveData<ArrayList<Boolean>> = MutableLiveData()
    //держит проперти пользовательские настройки по фильтровке и скрыванию карточек
    val filterProperties: MutableLiveData<Map<String,String?>> = MutableLiveData()

    init {
        viewModelScope.launch {
            propRepository.getAllProperties().collect{
                filterProperties.value = mutableMapOf<String, String?>().apply {
                    this[PropKey.catalogFilter_hideLearned.key] = it[PropKey.catalogFilter_hideLearned.key]
                    this[PropKey.catalogFilter_hideSleepingAndActive.key] = it[PropKey.catalogFilter_hideSleepingAndActive.key]

                    downloadAllWords()
                }
            }
        }

        //подгружаем из поискового обьекта найденные слова
        viewModelScope.launch {
            searchObject.listSearchWords.collect{
                listSearchWords.value = it
            }
        }

        downloadAllWords()
        downloadUsersWord()
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

    //скачает все слова из репозитория
    private fun downloadAllWords(){
        viewModelScope.launch {
            val listAllCards = cardsRepository.getAllWordCards()
            mapWordsByTopic.value = sortWordsByTopic(filterByProperties(listAllCards))
            namesTopics.value = fillNamesTopics(mapWordsByTopic.value as MutableMap<String, ArrayList<WordCard>>)
        }
    }
    //достанет из репозитория все карточки пользователя
    private fun downloadUsersWord(){
        viewModelScope.launch {
            mapUserCards.value = cardsRepository.getMapMyCards()
            namesTopicsUserCards.value = mapUserCards.value?.keys?.toList()
        }
    }

    /**
     * функция отфильтрует карточки если пользователь решил скрыть из каталога какие-нибудь выученные или активные
     */
    private fun filterByProperties(listAllCards: List<WordCard>): List<WordCard> {
        val mapProp: Map<String, String?>? = filterProperties.value
        val hideLearned:Boolean = mapProp?.get(PropKey.catalogFilter_hideLearned.key).toBoolean()
        val hideSleepingAndActive:Boolean = mapProp?.get(PropKey.catalogFilter_hideSleepingAndActive.key).toBoolean()
        if (mapProp!=null){
            return listAllCards
                .filter {//фильтруем по полю hideLearned если пользователь захотел скрыть изученные карточки
                    if (hideLearned){
                        return@filter !it.progressWord?.statProgress.equals(StatProgress.LEARNED.value)
                    }else return@filter true
                }
                .filter {//фильтруем по полю hideSleepingAndActive если пользователь захотел скрыть спящие и выученные карточки
                    if (hideSleepingAndActive){
                        return@filter !(it.progressWord?.statProgress.equals(StatProgress.NEW.value)
                                ||it.progressWord?.statProgress.equals(StatProgress.PARTIALLY_LEARNED.value)
                                ||it.progressWord?.statProgress.equals(StatProgress.ALMOST_LEARNED.value))
                    }else return@filter true
                }
        }else return listAllCards
    }

    /**
     * обновит списки слов и папок
     */
    fun refreshData() {
        downloadAllWords()
        downloadUsersWord()
    }
}