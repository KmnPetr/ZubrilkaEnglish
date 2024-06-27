package com.example.zubrilkaenglish.onlineCompetition

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class CompetitionViewModel : ViewModel() {
    private val competitionManager = CompetitionManager.instance
    val ping: MutableLiveData<Long?> = MutableLiveData(null)

    init {
        //вернет значение ping из NetworkHolder
        viewModelScope.launch{
            competitionManager.ping.collect{
                ping.postValue(it)
            }
        }
    }
}