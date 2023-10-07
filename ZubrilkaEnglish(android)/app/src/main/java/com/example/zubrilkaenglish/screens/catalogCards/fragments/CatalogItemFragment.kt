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
import com.example.zubrilkaenglish.screens.catalogCards.CatalogCardsViewModel

class CatalogItemFragment(
    override val viewModel_CC: CatalogCardsViewModel,
    override val positionInPager: Int,
    override val mapFoldersCards: MutableLiveData<Map<String, List<WordCard>>>,
    override val namesFolders: MutableLiveData<List<String>>
) : Fragment(), FragmentItem {
    private lateinit var binding: FragmentCatalogItemBinding
    override lateinit var folderAdapter: FoldersCardsAdapter
    override lateinit var cardAdapter: ListCardsAdapter
    override lateinit var recyclerView: RecyclerView
    override lateinit var viewLifecycleOwner_1: LifecycleOwner

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
        cardAdapter = ListCardsAdapter()
        viewLifecycleOwner_1 = viewLifecycleOwner

        recyclerView = binding.recyclerView
        recyclerView.adapter = folderAdapter

        namesFolders.observe(viewLifecycleOwner){list->
            val modifiedList = list.map { it+"   (слов: "+ (mapFoldersCards.value?.get(it)?.size ?: "null") + ")"}
            folderAdapter.setList(modifiedList)
        }
    }
}