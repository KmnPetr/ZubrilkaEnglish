package com.example.zubrilkaenglish.screens.training

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.zubrilkaenglish.databinding.FragmentTrainingBinding

class TrainingFragment : Fragment() {

    private lateinit var viewModel: TrainingViewModel
    private lateinit var binding: FragmentTrainingBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding=FragmentTrainingBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(TrainingViewModel::class.java)

        viewModel.getListWordsWithProgres().observe(viewLifecycleOwner){list->
            list.forEach {
                val text = "Слово: ${it.word.foreignWord}\t"+"Перевод: ${it.word.translation}\t"+"\n"+
                        "NumCorrAnsv: ${it.progressWord.numCorrAnsv}\t"+"StatProgress: ${it.progressWord.statProgress}\t"+"SleepTime: ${it.progressWord.sleepTime}\t"+"\n"
                binding.listWordsWithProgress.append(text)
            }
        }
    }

}