package com.example.zubrilkaenglish.screens.myCards

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.zubrilkaenglish.databinding.FragmentMyCardsBinding
import com.example.zubrilkaenglish.utils.customizeBackground

class MyCardsFragment : Fragment() {

    private lateinit var viewModel: MyCardsViewModel
    private lateinit var binding: FragmentMyCardsBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: MyCardsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMyCardsBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(MyCardsViewModel::class.java)
        recyclerView = binding.recyclerView
        adapter = MyCardsAdapter(findNavController())
        recyclerView.adapter = adapter

        customizeBackground(binding.background,resources)

        viewModel.getMapMyCards().observe(viewLifecycleOwner){map->
            val namesFolders = map.keys.toList().map {  it+"   (слов: "+ (map[it]?.size?: "null") + ")"  }
            adapter.setList(namesFolders)
        }
    }
}