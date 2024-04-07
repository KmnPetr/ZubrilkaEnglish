package com.example.ze_adminandroid.repositories

import androidx.lifecycle.MutableLiveData
import com.example.ze_adminandroid.events.VcEvEnum
import com.example.ze_adminandroid.events.VoiceEvent
import com.example.ze_adminandroid.models.Word
import com.example.ze_adminandroid.services.RetrofitService
import com.example.ze_adminandroid.services.RoomService
import com.example.zubrilkaenglish.events.WordEvent
import com.example.zubrilkaenglish.events.WrEvEnum
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

class WordRepository private constructor(){


    companion object{
        val instance: WordRepository by lazy { WordRepository() }
    }

    private val retrofitService = RetrofitService.instance
    private var roomService: RoomService = RoomService()
    private val listWords: MutableStateFlow<List<Word>> = MutableStateFlow(arrayListOf())
    val filteredListAllWords: MutableStateFlow<List<Word>> = MutableStateFlow(arrayListOf())
    val setEditedWordId: MutableStateFlow<HashSet<Int>> = MutableStateFlow(hashSetOf()) //содержит набор id уже редактированных word для фильтрации основного списка слов
    val notReadyWord: MutableStateFlow<Word?> = MutableStateFlow(null)
    //вернет количество сущностей  Word из БД
    val countWords: MutableStateFlow<Int?> = MutableStateFlow(null)

    init {
        EventBus.getDefault().register(this)

        GlobalScope.launch(Dispatchers.Default) {
            val list: List<Word>? = retrofitService.getAllWordsFromNetwork()
            if (list != null) {
                println("ПОЛУЧЕН СПИСОК С СЕТИ. Размер: "+list.size)
                listWords.value = list
            }
        }
        checkNotReadyWord()

        GlobalScope.launch {
            roomService.getEditedWordDAO().getCount().collect{count ->
                countWords.value = count
            }
        }

        //заполним set значениями id из списка редактированных слов
        GlobalScope.launch {
            roomService.getEditedWordDAO().getAllEditedWords().collect{list->
                setEditedWordId.value = fillSetId(list)
            }
        }

        //в случае изменения сета айдишников заставим перефильтровать список
        GlobalScope.launch {
            setEditedWordId.collect{set->
                filterList(listWords.value,set)
            }
        }
        //в случае изменения списка всех слов заставим перефильтровать список
        GlobalScope.launch {
            listWords.collect{listWords->
                filterList(listWords,setEditedWordId.value)
            }
        }
    }

    /**
     * метод используется библиотечкой EventBus
     * для прослушивания запросов от различных view
     */
    @Subscribe
    fun subscribeOnWordEvent(event: WordEvent){
        when(event.typeEvent){
            WrEvEnum.SET_VOICE_VERIFIED -> {
                GlobalScope.launch{
                    roomService.getEditedWordDAO().setVoiceVerified(event.word.localBaseId,event.word.voiceVerified)
                }
            }
            WrEvEnum.DELETE_FROM_DATABASE -> {
                GlobalScope.launch{
                    deleteWord(event.word.localBaseId)
                }
            }
            else -> {}
        }
    }

    /**
     * отфильтрует список слов,
     * чтобы в нем не попадались word c id уже редактированных слов,
     * а также слов имеющих значение в поле link_voice
     */
    private fun filterList(listAllWords: List<Word>, setId: HashSet<Int>) {
        val filteredList: MutableList<Word> = mutableListOf()
        listAllWords.forEach {
            if(it.link_voice == null || it.link_voice.equals("")){
                if (!setId.contains(it.id)){
                    filteredList.add(it)
                }
            }
        }
        filteredListAllWords.value = filteredList
    }


    /**
     * заполнит сет значениями id из переданных ему списка words
     */
    private fun fillSetId(words: List<Word>): HashSet<Int> {
        val set = HashSet<Int>()
        words.forEach {
            it.id?.let { it1 -> set.add(it1) }
        }
        return set
    }

    /**
     * выдаст из БД незавершеное слово, которое не было закончено в течении минуты назад
     * т.к. при переходе по ссылке в браузере для поиска озвучки, приложение сбрасывает накопленный прогресс редактирования по слову
     * поэтому требуется его соохранение в БД в неготовом виде
     */
    private fun checkNotReadyWord() {
        GlobalScope.launch(Dispatchers.Default) {
            roomService.getNotReadyWords()?.forEach {
                if (it.time_last_update!=null && it.time_last_update!! > (System.currentTimeMillis()-60000)){
                    withContext(Dispatchers.Main){
                        notReadyWord.value = it
                    }
                }
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

    /**
     * выдаст Flow для списка всех обьектов Word в БД
     */
    fun getFlowAllEditedWords():Flow<List<Word>> = roomService.getFlowAllEditedWords()
    suspend fun getFirstWord(): Word? = roomService.getEditedWordDAO().getFirstWord()
    suspend fun deleteWord(localBaseId: Int?) {
        roomService.getEditedWordDAO().deleteWord(localBaseId)
    }
}