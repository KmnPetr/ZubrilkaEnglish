package com.example.zubrilkaenglish.screens.wordDetails

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import com.example.zubrilkaenglish.R
import com.example.zubrilkaenglish.databinding.FragmentWordDetailsBinding
import com.example.zubrilkaenglish.utils.MYBUNDLE
import com.example.zubrilkaenglish.utils.SIM_FORM_DATE
import com.example.zubrilkaenglish.utils.StatProgress
import java.text.SimpleDateFormat
import java.util.Date

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

        //довольно важная строчка
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

        //увеличивает размер кнопки при нажатии
        binding.options.setOnTouchListener { view, motionEvent ->
            when (motionEvent.action) {
                MotionEvent.ACTION_DOWN -> {
                    view.animate().scaleX(1.2f).scaleY(1.2f).setDuration(150).start()
                }
                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                    view.animate().scaleX(1f).scaleY(1f).setDuration(150).start()
                }
            }
            false
        }
    }

    /**
     * функция покажет всплывающий набор опций над выбранным словом
     */
    private fun showPopUpOptions() {

        val popupMenu = PopupMenu(requireActivity(),binding.options)
        popupMenu.inflate(R.menu.optionsword_menu)
        val addToTrain = popupMenu.menu.findItem(R.id.addToTrain)
        val resetProgress = popupMenu.menu.findItem(R.id.resetProgress)
        val markLearned = popupMenu.menu.findItem(R.id.markLearned)
        val deleteCard = popupMenu.menu.findItem(R.id.deleteCard)

        viewModel.getWordCard().observe(viewLifecycleOwner){wordCard->
            if(wordCard.progressWord!=null) {
                addToTrain.isEnabled = false
                addToTrain.isVisible = false
                if(wordCard.progressWord?.statProgress == StatProgress.LEARNED.value){
                    markLearned.isEnabled = false
                }else markLearned.isEnabled = true
                if(wordCard.progressWord?.statProgress==StatProgress.NEW.value&&
                    wordCard.progressWord?.numCorrAnsv==0&&
                    compareDate(wordCard.progressWord?.sleepTime)){
                    resetProgress.isEnabled = false
                }else resetProgress.isEnabled = true
            }else{
                resetProgress.isEnabled = false
                resetProgress.isVisible = false
                markLearned.isEnabled = false
                markLearned.isVisible = false
                deleteCard.isEnabled = false
                deleteCard.isVisible = false
            }

        }

        popupMenu.setOnMenuItemClickListener { item->
            when(item.itemId){
                R.id.addToTrain->{
                    viewModel.addWordToTraining()
                    true
                }
                R.id.resetProgress->{
                    viewModel.resetProgressCard()
                    true
                }
                R.id.markLearned->{
                    viewModel.markCardLearned()
                    true
                }
                R.id.deleteCard->{
                    viewModel.deleteCard()
                    true
                }
                else-> false
            }
        }

        popupMenu.show()
    }

    /**
     * функция вернет false, если входящая в параметры дата еще не наступила
     */
    private fun compareDate(sleepTime: String?): Boolean{
        try {
            if (sleepTime==null){
                return true
            }else if(SimpleDateFormat(SIM_FORM_DATE).parse(sleepTime).before(Date())){
                return true
            }
        } catch (e: Exception) {
            return false
        }
        return false
    }
}