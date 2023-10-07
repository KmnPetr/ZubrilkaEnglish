package com.example.zubrilkaenglish.screens.catalogCards.fragments

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.example.zubrilkaenglish.models.WordCard
import com.example.zubrilkaenglish.screens.catalogCards.CatalogCardsViewModel


interface FragmentItem {
    val viewModel_CC: CatalogCardsViewModel
    val positionInPager: Int
    val mapFoldersCards: MutableLiveData<Map<String, List<WordCard>>>
    val namesFolders: MutableLiveData<List<String>>
    val recyclerView: RecyclerView
    val folderAdapter: FoldersCardsAdapter
    val cardAdapter: ListCardsAdapter
    val viewLifecycleOwner_1: LifecycleOwner
    fun onClickFolder(positionFolder: Int){
        recyclerView.adapter = cardAdapter

        viewModel_CC.isRecyclerChanged.value?.set(positionInPager,true)

        mapFoldersCards.observe(viewLifecycleOwner_1){
            it[namesFolders.value?.get(positionFolder)]?.let { it1 -> cardAdapter.setList(it1) }
        }
    }
    fun rollBackRecycler(){
        recyclerView.adapter = folderAdapter
    }
}