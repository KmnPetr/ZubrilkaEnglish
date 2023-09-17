package com.example.zubrilkaenglish.screens.myCards.listCards

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.zubrilkaenglish.R
import com.example.zubrilkaenglish.databinding.FragmentListCardsBinding
import com.example.zubrilkaenglish.screens.myCards.MyCardsViewModel
import com.example.zubrilkaenglish.utils.MYBUNDLE

class ListCardsFragment : Fragment() {

    private lateinit var myCardsViewModel: MyCardsViewModel
    private lateinit var binding: FragmentListCardsBinding
    private lateinit var adapter: ListCardsAdapter
    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentListCardsBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        myCardsViewModel = ViewModelProvider(this).get(MyCardsViewModel::class.java)
        adapter = ListCardsAdapter(findNavController())
        recyclerView = binding.recyclerView
        recyclerView.adapter = adapter

        myCardsViewModel.getMapMyCards().observe(viewLifecycleOwner){map->
            val numNameFolder: Int? = MYBUNDLE.get("number_position_into_list")
            val nameFolder: String = map.keys.toList()[numNameFolder!!]
            map[nameFolder]?.let { adapter.setList(it) }
        }
    }
}