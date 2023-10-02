package com.example.zubrilkaenglish.screens.catalogCards.fragments.SearchCardFragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.zubrilkaenglish.databinding.FragmentSearchCardBinding
import com.example.zubrilkaenglish.screens.catalogCards.CatalogCardsViewModel

class SearchCardFragment(viewModel: CatalogCardsViewModel) : Fragment() {

    private lateinit var binding: FragmentSearchCardBinding
    private lateinit var adapter: SearchCardAdapter
    private var viewModel_CC = viewModel
    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchCardBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = SearchCardAdapter()
        recyclerView = binding.recyclerView
        recyclerView.adapter = adapter

        viewModel_CC.listSearchWords.observe(viewLifecycleOwner){
            adapter.setList(it)
            println("СРАБОТАЛ ОБСЕРВЕР")
        }
    }
}