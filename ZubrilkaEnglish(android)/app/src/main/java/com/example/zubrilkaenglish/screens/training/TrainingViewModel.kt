package com.example.zubrilkaenglish.screens.training

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.zubrilkaenglish.models.WordWithProgress
import com.example.zubrilkaenglish.repositories.Repository
import kotlinx.coroutines.launch

class TrainingViewModel : ViewModel() {

    private val repository = Repository()

    private val listWordsWithProgress:MutableLiveData<List<WordWithProgress>> = MutableLiveData()

    fun getListWordsWithProgres(): MutableLiveData<List<WordWithProgress>> {
        if (listWordsWithProgress.value==null){
            viewModelScope.launch {
                listWordsWithProgress.value = repository.getAllWordsWithProgress()
            }
        }
        return listWordsWithProgress
    }
}