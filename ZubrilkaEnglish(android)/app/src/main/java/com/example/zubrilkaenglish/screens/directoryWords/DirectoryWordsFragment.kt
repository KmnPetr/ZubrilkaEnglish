package com.example.zubrilkaenglish.screens.directoryWords

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.zubrilkaenglish.databinding.FragmentDirectoryWordsBinding
import com.example.zubrilkaenglish.screens.activity.MainViewModel

class DirectoryWordsFragment : Fragment() {

    private lateinit var mainViewModel: MainViewModel
    private lateinit var viewModel: DirectoryWordsViewModel
    private lateinit var binding: FragmentDirectoryWordsBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: DirectoryWordsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDirectoryWordsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(DirectoryWordsViewModel::class.java)
        mainViewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
        initButtons()
        recyclerView = binding.rwDirectoryWords
        adapter = DirectoryWordsAdapter(findNavController())
        recyclerView.adapter = adapter

        mainViewModel.namesTopics.observe(viewLifecycleOwner){list->
            val modifiedList = list.map { it+"   (слов: "+ (mainViewModel.mapWordsByTopic.value?.get(it)?.size ?: "null") + ")"}
            adapter.setList(modifiedList)
        }

    }

    private fun initButtons() {
        binding.buttonDirBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }
}