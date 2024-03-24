package com.example.ze_adminandroid.repositories

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
    }
    /**
     * метод используется библиотечкой EventBus
     * для прослушивания запросов от различных view
     */
    @Subscribe
    fun subscribeOnVoiceEvent(event: WordEvent){
        when(event.typeEvent){
            WrEvEnum.SET_VOICE_VERIFIED -> {
                GlobalScope.launch{
                    roomService.getEditedWordDAO().setVoiceVerified(event.word.localBaseId,event.word.voiceVerified)
                }
            }
            else -> {}
        }
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