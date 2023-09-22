package com.example.zubrilkaenglish.screens.directoryWords.listlWords

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.zubrilkaenglish.databinding.FragmentListWordsBinding
import com.example.zubrilkaenglish.models.Word
import com.example.zubrilkaenglish.models.WordCard
import com.example.zubrilkaenglish.screens.activity.MainViewModel
import com.example.zubrilkaenglish.utils.MYBUNDLE

class ListWordsFragment : Fragment() {

    private lateinit var viewModel: ListWordsViewModel
    private lateinit var mainViewModel: MainViewModel
    private lateinit var binding: FragmentListWordsBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ListWordsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentListWordsBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(ListWordsViewModel::class.java)
        mainViewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
        recyclerView = binding.rvlistWords
        adapter = ListWordsAdapter(findNavController())
        recyclerView.adapter = adapter
        getListWords()?.let { adapter.setList(it) }
    }

    /**
     * функция раздобудет список Words
     * согласно преданной информации в MYBUNDLE из класса вызвавшаго этот фрагмент
     */
    private fun getListWords(): ArrayList<WordCard>? {
        val numberPosition = MYBUNDLE.get("number_position_into_list")
        val keyTopic = mainViewModel.namesTopics.value!![numberPosition!!]
        val listWords = mainViewModel.mapWordsByTopic.value?.get(keyTopic)
        return listWords
    }
}