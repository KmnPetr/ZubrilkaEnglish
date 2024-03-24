package com.example.ze_adminandroid.screens.serverConnect

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ze_adminandroid.models.Word
import com.example.ze_adminandroid.repositories.VoiceRepository
import com.example.ze_adminandroid.repositories.WordRepository
import com.example.ze_adminandroid.screens.serverConnect.socketService.NetworkHolder
import kotlinx.coroutines.launch

class ServerConnectViewModel : ViewModel() {
    private val wordRepository = WordRepository.instance
    private val voiceRepository = VoiceRepository.instance
    private val networkHolder = NetworkHolder.instance

    //в строке хост, в значении если true значит хост прочекивается
    val host: MutableLiveData<Pair<String,Boolean>?> = MutableLiveData()
    //в строке значение из изменяемого EditText
    val editedHost: MutableLiveData<String> = MutableLiveData()
    val ping: MutableLiveData<Long> = MutableLiveData()

    val listEditedWords: MutableLiveData<List<Word>> = MutableLiveData()
    val countVoices: MutableLiveData<Int> = MutableLiveData()
    val countWords: MutableLiveData<Int> = MutableLiveData()

    init {
        //список измененных слов подгружает Flow при любом изменении в БД
        viewModelScope.launch {
            wordRepository.getFlowAllEditedWords().collect { listWords ->
                listEditedWords.postValue(listWords)
            }
        }


        //вернет количество сущностей  Voice  из БД
        viewModelScope.launch{
            voiceRepository.countVoices.collect{
                countVoices.postValue(it)
            }
        }
        //вернет количество сущностей  Word  из БД
        viewModelScope.launch{
            wordRepository.countWords.collect{
                countWords.postValue(it)
            }
        }


        //вернет значение host из NetworkHolder
        viewModelScope.launch{
            networkHolder.host.collect{
                host.postValue(it)
            }
        }

        //вернет значение ping из NetworkHolder
        viewModelScope.launch{
            networkHolder.ping.collect(){
                ping.postValue(it)
            }
        }
    }
}