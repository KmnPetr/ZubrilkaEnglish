package com.example.zubrilkaenglish.screens.catalogCards.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.example.zubrilkaenglish.databinding.FragmentCatalogItemBinding
import com.example.zubrilkaenglish.models.WordCard
import com.example.zubrilkaenglish.screens.catalogCards.CatalogCardsFragment
import com.example.zubrilkaenglish.screens.catalogCards.CatalogCardsViewModel

class CatalogItemFragment(
    val viewModel_CC: CatalogCardsViewModel,
    val positionInPager: Int,
    val mapFoldersCards: MutableLiveData<Map<String, List<WordCard>>>,
    val namesFolders: MutableLiveData<List<String>>,
    override val owner: CatalogCardsFragment
) : Fragment(), FragmentItem {
    private lateinit var binding: FragmentCatalogItemBinding
    lateinit var folderAdapter: FoldersCardsAdapter
    lateinit var cardAdapter: ListCardsAdapter
    lateinit var recyclerView: RecyclerView
    lateinit var viewLifecycleOwner_1: LifecycleOwner

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCatalogItemBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        folderAdapter = FoldersCardsAdapter(this)
        cardAdapter = ListCardsAdapter(this)
        viewLifecycleOwner_1 = viewLifecycleOwner

        recyclerView = binding.recyclerView
        recyclerView.adapter = folderAdapter

        namesFolders.observe(viewLifecycleOwner){list->
            val modifiedList = list.map { it+"   (слов: "+ (mapFoldersCards.value?.get(it)?.size ?: "null") + ")"}
            folderAdapter.setList(modifiedList)
        }
    }

    /**
     * выполняется при нажатии на элемент папки
     */
    override fun onClickFolder(positionFolder: Int) {
        recyclerView.adapter = cardAdapter

        viewModel_CC.isRecyclerChanged.value?.set(positionInPager,true)

        mapFoldersCards.observe(viewLifecycleOwner_1){
            it[namesFolders.value?.get(positionFolder)]?.let { it1 -> cardAdapter.setList(it1) }
        }
    }

    /**
     * выполняется если надо вернуться от списка карочек к списку папок
     */
    override fun rollBackRecycler() {
        recyclerView.adapter = folderAdapter
    }
}