package com.example.ze_adminandroid.ui.catalogWords.fragments

import com.example.ze_adminandroid.ui.catalogWords.CatalogWordsFragment

interface FragmentItem {
    val owner: CatalogWordsFragment
    fun onClickFolder(positionFolder: Int)
    fun rollBackRecycler()
}