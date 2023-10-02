package com.example.zubrilkaenglish.screens.catalogCards

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.zubrilkaenglish.models.WordCard
import com.example.zubrilkaenglish.utils.SearchObject

class CatalogCardsViewModel : ViewModel() {
    var searchCreated: Boolean = false //указывает, создан ли ранее фрагмент поиска слова
    var lastPositionTablayout: Int = 0 //указ.последний используемый фрагмент для возврата на него после удаления поискового фрагмента
    val listSearchWords: MutableLiveData<List<WordCard>> = MutableLiveData()


    /**
     * будет изменять список при вводе в поисковую строку нового слова
     */
    fun changeListSearchWord(word: String){
        listSearchWords.value = SearchObject.instance.search(word)
        println("СМЕНА СПИСКА. РАЗМЕР СПИСКА: "+ listSearchWords.value?.size)
    }
}