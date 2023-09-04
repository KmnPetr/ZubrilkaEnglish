package com.example.zubrilkaenglish.screens.listlWords

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ListWordsViewModel : ViewModel() {
    var keyTopic: MutableLiveData<String> = MutableLiveData()
}