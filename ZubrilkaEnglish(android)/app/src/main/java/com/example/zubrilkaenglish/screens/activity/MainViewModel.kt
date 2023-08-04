package com.example.zubrilkaenglish.screens.activity

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.zubrilkaenglish.repositories.Repository
import com.example.zubrilkaenglish.models.Word
import kotlinx.coroutines.launch

class MainViewModel: ViewModel() {

    private val repository= Repository()

    private val listAllWords: MutableLiveData<List<Word>> = MutableLiveData()

    /**
     * функция вернет список Words и если он пустой сделает запрос в репозиторий
     */
    fun getListAllWords(): LiveData<List<Word>> {
        if(listAllWords.value==null){
            viewModelScope.launch {
                listAllWords.value=repository.getAllWords()
            }
        }
        return listAllWords
    }
}