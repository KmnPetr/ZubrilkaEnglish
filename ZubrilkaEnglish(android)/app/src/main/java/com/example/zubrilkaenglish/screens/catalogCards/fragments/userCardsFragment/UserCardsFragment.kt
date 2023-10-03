package com.example.zubrilkaenglish.screens.catalogCards.fragments.userCardsFragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.zubrilkaenglish.databinding.FragmentUserCardsBinding
import com.example.zubrilkaenglish.screens.catalogCards.CatalogCardsViewModel

class UserCardsFragment(viewModel: CatalogCardsViewModel) : Fragment() {
    private lateinit var binding: FragmentUserCardsBinding
    private lateinit var adapter: UserCardsAdapter
    private var viewModel_CC = viewModel
    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUserCardsBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = UserCardsAdapter()
        recyclerView = binding.recyclerView
        recyclerView.adapter = adapter

        viewModel_CC.namesTopicsUserCards.observe(viewLifecycleOwner){list->
            val modifiedList = list.map { it+"   (слов: "+ (viewModel_CC.mapUserCards.value?.get(it)?.size ?: "null") + ")"}
            adapter.setList(modifiedList)
        }
    }
}