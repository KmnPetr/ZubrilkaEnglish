package com.example.zubrilkaenglish.screens.memo

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.zubrilkaenglish.models.Memo
import com.example.zubrilkaenglish.repositories.MemoRepository
import kotlinx.coroutines.launch

class MemoViewModel : ViewModel() {
    val memoRepository = MemoRepository.instance
    val allMemos: MutableLiveData<List<Memo>> = MutableLiveData()
    init {
        viewModelScope.launch {
            memoRepository.getAllMemos().collect{
                allMemos.value = it
            }
        }
    }
}