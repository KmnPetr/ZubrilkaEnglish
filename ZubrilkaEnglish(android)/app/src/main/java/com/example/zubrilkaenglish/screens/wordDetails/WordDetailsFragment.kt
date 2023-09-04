package com.example.zubrilkaenglish.screens.wordDetails

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.zubrilkaenglish.databinding.FragmentWordDetailsBinding
import com.example.zubrilkaenglish.utils.MYBUNDLE

class WordDetailsFragment : Fragment() {

    private lateinit var viewModel: WordDetailsViewModel
    private lateinit var binding: FragmentWordDetailsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentWordDetailsBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(WordDetailsViewModel::class.java)

        viewModel.idWord.value = MYBUNDLE["id_pressed_word"]

        viewModel.getWordDetails().observe(viewLifecycleOwner){word->
            binding.foreignWord.text = word.foreignWord
            binding.transcription.text = word.transcription
            binding.translation.text = word.translation
            binding.description.text = word.description
            binding.groupWord.text = "Группа: "+word.groupWord

            //инициализацию кнопок лучше не поднимать выше предыдущих действий
            initButtons()
        }
    }
    fun initButtons(){
        binding.addTraining.setOnClickListener {
                viewModel.addWordToTraining()
        }
    }
}