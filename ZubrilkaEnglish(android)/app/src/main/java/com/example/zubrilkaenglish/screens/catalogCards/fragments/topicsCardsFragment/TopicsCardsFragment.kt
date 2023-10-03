package com.example.zubrilkaenglish.screens.catalogCards.fragments.topicsCardsFragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.zubrilkaenglish.R
import com.example.zubrilkaenglish.databinding.FragmentTopicsCardsBinding
import com.example.zubrilkaenglish.screens.catalogCards.CatalogCardsViewModel
import com.example.zubrilkaenglish.screens.catalogCards.fragments.searchCardFragment.SearchCardAdapter

class TopicsCardsFragment(viewModel: CatalogCardsViewModel) : Fragment() {

    private lateinit var binding: FragmentTopicsCardsBinding
    private lateinit var adapter: TopicsCardsAdapter
    private var viewModel_CC = viewModel
    private lateinit var recyclerView: RecyclerView


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTopicsCardsBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = TopicsCardsAdapter()
        recyclerView = binding.recyclerView
        recyclerView.adapter = adapter

        viewModel_CC.namesTopics.observe(viewLifecycleOwner){list->
            val modifiedList = list.map { it+"   (слов: "+ (viewModel_CC.mapWordsByTopic.value?.get(it)?.size ?: "null") + ")"}
            adapter.setList(modifiedList)
        }
    }
}