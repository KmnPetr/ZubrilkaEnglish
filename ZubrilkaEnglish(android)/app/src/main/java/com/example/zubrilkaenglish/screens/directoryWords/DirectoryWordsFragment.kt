package com.example.zubrilkaenglish.screens.directoryWords

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.zubrilkaenglish.databinding.FragmentDirectoryWordsBinding
import com.example.zubrilkaenglish.screens.activity.MainViewModel
import com.example.zubrilkaenglish.utils.SearchObject

class DirectoryWordsFragment : Fragment() {

    private lateinit var mainViewModel: MainViewModel
    private lateinit var viewModel: DirectoryWordsViewModel
    private lateinit var binding: FragmentDirectoryWordsBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: DirectoryWordsAdapter
    private lateinit var searchRecyclerView: RecyclerView
    private lateinit var searchAdapter: SearchWordCardAdapter

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

        searchRecyclerView = binding.searchRecyclerView
        searchAdapter = SearchWordCardAdapter(findNavController())
        searchRecyclerView.adapter = searchAdapter

        //необходима инициализация и загрузка списка из БД в этот обьект заранее
        SearchObject.instance

        mainViewModel.namesTopics.observe(viewLifecycleOwner){list->
            val modifiedList = list.map { it+"   (слов: "+ (mainViewModel.mapWordsByTopic.value?.get(it)?.size ?: "null") + ")"}
            adapter.setList(modifiedList)
        }

    }

    private fun initButtons() {
        binding.searchEditText.addTextChangedListener(object : TextWatcher {
            // Выполняется перед изменением текста в EditText
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            // Выполняется во время изменения текста в EditText
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val updatedText = s.toString() // Получить новый текст из CharSequence
                if(updatedText.trim().isNotEmpty()){
                    binding.rwDirectoryWords.visibility = View.GONE
                    binding.searchRecyclerView.visibility = View.VISIBLE
                    searchAdapter.setList(SearchObject.instance.search(updatedText))

                }else{
                    println("recycler view has been restored") // Вывести текст в консоль или логи
                    binding.rwDirectoryWords.visibility = View.VISIBLE
                    binding.searchRecyclerView.visibility = View.GONE
                }
            }

            // Выполняется после изменения текста в EditText
            override fun afterTextChanged(s: Editable?) {}
        })
    }
}