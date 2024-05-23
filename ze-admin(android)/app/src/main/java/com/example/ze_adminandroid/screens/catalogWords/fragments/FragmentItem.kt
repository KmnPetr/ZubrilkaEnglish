package com.example.ze_adminandroid.screens.catalogWords.fragments

import com.example.ze_adminandroid.models.Word
import com.example.ze_adminandroid.screens.catalogWords.CatalogWordsFragment

interface FragmentItem {
    val owner: CatalogWordsFragment
    fun onClickFolder(positionFolder: Int)

    fun onClickWord(word: Word)
    fun rollBackRecycler()
    fun onClickButtonPlay(word: Word)
    fun onClickButtonDelete(word: Word)
    fun setListAdapter()
}