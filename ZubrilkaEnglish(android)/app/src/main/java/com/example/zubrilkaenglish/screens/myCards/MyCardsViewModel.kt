package com.example.zubrilkaenglish.screens.myCards

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.zubrilkaenglish.models.WordCard
import com.example.zubrilkaenglish.repositories.Repository
import kotlinx.coroutines.launch

class MyCardsViewModel : ViewModel() {

    val repository = Repository()

    private val mapMyCards: MutableLiveData<Map<String,List<WordCard>>> = MutableLiveData()
    private val namesFolders: MutableLiveData<List<String>> = MutableLiveData()


    fun getMapMyCards(): MutableLiveData<Map<String,List<WordCard>>>{
        viewModelScope.launch {
            mapMyCards.value = repository.getMapMyCards()
        }
        return mapMyCards
    }
}