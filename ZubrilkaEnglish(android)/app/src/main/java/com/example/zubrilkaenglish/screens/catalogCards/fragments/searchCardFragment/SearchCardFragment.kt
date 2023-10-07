package com.example.zubrilkaenglish.screens.catalogCards.fragments.searchCardFragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.zubrilkaenglish.databinding.FragmentSearchCardBinding
import com.example.zubrilkaenglish.screens.catalogCards.CatalogCardsFragment
import com.example.zubrilkaenglish.screens.catalogCards.CatalogCardsViewModel
import com.example.zubrilkaenglish.screens.catalogCards.fragments.FragmentItem
import com.example.zubrilkaenglish.screens.catalogCards.fragments.ListCardsAdapter

class SearchCardFragment(
    viewModel: CatalogCardsViewModel,
    override val owner: CatalogCardsFragment
) : Fragment(),FragmentItem {

    private lateinit var binding: FragmentSearchCardBinding
    private lateinit var adapter: ListCardsAdapter
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
        adapter = ListCardsAdapter(this)
        recyclerView = binding.recyclerView
        recyclerView.adapter = adapter

        viewModel_CC.listSearchWords.observe(viewLifecycleOwner){
            adapter.setList(it)
        }
    }

    //излишний для данного фрагмента метод
    override fun onClickFolder(positionFolder: Int) {
        TODO("Not yet implemented")
    }


    //излишний для данного фрагмента метод
    override fun rollBackRecycler() {
        TODO("Not yet implemented")
    }

}