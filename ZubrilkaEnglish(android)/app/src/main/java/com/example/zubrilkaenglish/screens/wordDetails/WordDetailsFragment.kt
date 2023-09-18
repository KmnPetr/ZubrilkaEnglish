package com.example.zubrilkaenglish.screens.wordDetails

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.zubrilkaenglish.R
import com.example.zubrilkaenglish.databinding.FragmentWordDetailsBinding
import com.example.zubrilkaenglish.utils.MYBUNDLE
import com.example.zubrilkaenglish.utils.MyApplication

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
        }
        //инициализацию кнопок лучше не поднимать выше предыдущих действий
        initButtons()
    }
    fun initButtons(){
        binding.options.setOnClickListener {
            showPopUpOptions()
        }
        binding.addTraining.setOnClickListener {
                viewModel.addWordToTraining()
        }
    }

    /**
     * функция покажет всплывающий набор опций над выбранным словом
     */
    private fun showPopUpOptions() {


        val popupMenu = PopupMenu(requireActivity(),binding.options)
        popupMenu.inflate(R.menu.optionsword_menu)

        popupMenu.setOnMenuItemClickListener { item->
            when(item.itemId){
                R.id.addToTrain->{
                    viewModel.addWordToTraining()
                    true
                }
                else-> false
            }
        }

        popupMenu.show()
    }
}