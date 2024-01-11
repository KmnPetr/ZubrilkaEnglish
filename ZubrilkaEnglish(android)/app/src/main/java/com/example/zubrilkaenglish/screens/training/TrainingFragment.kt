package com.example.zubrilkaenglish.screens.training

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
import android.widget.Button
import android.widget.TextView
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.example.zubrilkaenglish.R
import com.example.zubrilkaenglish.databinding.FragmentTrainingBinding
import com.example.zubrilkaenglish.models.ICard
import com.example.zubrilkaenglish.models.NewsCard
import com.example.zubrilkaenglish.models.WordCard
import com.example.zubrilkaenglish.utils.SIM_FORM_DATE
import com.example.zubrilkaenglish.utils.StatProgress
import com.google.android.material.slider.Slider
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date

class TrainingFragment : Fragment(), CardAdapter.Listener {

    private lateinit var viewModel: TrainingViewModel
    private lateinit var binding: FragmentTrainingBinding
    private val adapter = CardAdapter(this)
    private var countWordCards: Int = 0
    private lateinit var countCards : TextView


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
        countCards = binding.countCards


        binding.viewPager2.adapter=adapter

        binding.viewPager2.registerOnPageChangeCallback(object: ViewPager2.OnPageChangeCallback() {
            override fun onPageScrollStateChanged(state: Int) {
                super.onPageScrollStateChanged(state)
                viewModel.userScrolls = 0
            }
        })

        /**
         * Функция заполняет cardList адаптера
         */
        viewModel.getWordsCards().observe(viewLifecycleOwner){listWordCard->
            var listForTreining = ArrayList<ICard>()
            listWordCard.forEach { it ->
                if (it.progressWord?.statProgress!=StatProgress.LEARNED.value&&compareDate(it.progressWord?.sleepTime)){
                    listForTreining.add(it)
                    countWordCards++

                }
            }
            listForTreining.add(NewsCard("news will be here"))

            adapter.setList(listForTreining)

            countCards.text = "${binding.viewPager2.currentItem + 1} / $countWordCards"
        }

        showCountCards()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        /*посоветовали отменить регистрацию
        binding.pager.unregisterOnPageChangeCallback(this)*/ //TODO
    }

    /**
     * слушатель при нажатии на кнопку "Yes"
     * если пользователь подтверждает, что знает карточку
     */
    override fun onClickYesButton(wordCard: WordCard) {
        wordCard.cardHasChanged=true

        if (wordCard.progressWord?.numCorrAnsv!! >=3){
            //обновляем значение numCorrAnsv в viewModel и в репозитории
            wordCard.progressWord!!.numCorrAnsv = viewModel.plusCorAnsv(wordCard.progressWord!!.wordId)?.progressWord?.numCorrAnsv!!
            adapter.notifyItemChanged(binding.viewPager2.currentItem)
            //показываем окошко диалога
            showPopUpDialog(wordCard)
        }else{
            //обновляем значение numCorrAnsv в viewModel и в репозитории
            wordCard.progressWord!!.numCorrAnsv = viewModel.plusCorAnsv(wordCard.progressWord!!.wordId)?.progressWord?.numCorrAnsv!!
            adapter.notifyItemChanged(binding.viewPager2.currentItem)
            flippingCard()
        }
    }

    /**
     * слушатель при нажатии на кнопку "No"
     * если пользователь не узнает карточку
     */
    override fun onClickNoButton(wordCard: WordCard) {
        wordCard.cardHasChanged=true

        val updatedWordCard = viewModel.resetCorAnsv(wordCard.progressWord!!.wordId)
        wordCard.progressWord!!.numCorrAnsv = updatedWordCard?.progressWord?.numCorrAnsv!!


        adapter.notifyItemChanged(binding.viewPager2.currentItem)

        flippingCard()
    }

    /**
     * метод реагирует на нажатие кнопки Look
     */
    override fun onClickLookButton(wordCard: WordCard) {
        wordCard.lookButtonPressed=true
        adapter.notifyItemChanged(binding.viewPager2.currentItem)
    }

    /**
     * перелистывание фрагмента на следующий
     * с защитой от перелистывания во время скролла пальцем
     */
    private fun flippingCard(){
        viewModel.userScrolls =1
        GlobalScope.launch{
            delay(700L)
            if (viewModel.userScrolls !=0) {
                viewModel.userScrolls =0
                binding.viewPager2.setCurrentItem((binding.viewPager2.currentItem + 1),true)
            }
        }
    }

    /**
     * функция создаст popUp окошко
     */
    private fun showPopUpDialog(wordCard: WordCard) {
        val dialog = Dialog(requireActivity())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.popup_dialog)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val textDialog: TextView = dialog.findViewById(R.id.textDialog)
        val btnYes: Button = dialog.findViewById(R.id.btnYes)
        val btnCansel: Button = dialog.findViewById(R.id.btnCansel)
        val slider: Slider = dialog.findViewById(R.id.slider)

        textDialog.text = "Кажется вы уже запомнили эту карточку. Рекомендуем вам повторить ее спустя некоторое время. Карточка уснет на "+slider.value.toInt().toString()+ " дня(дней)."
        slider.addOnChangeListener { slider, value, fromUser ->
            textDialog.text = "Кажется вы уже запомнили эту карточку. Рекомендуем вам повторить ее спустя некоторое время. Карточка уснет на "+value.toInt().toString()+ " дня(дней)."
        }
        //Настройка слайдера
        when(wordCard.progressWord?.statProgress){
            StatProgress.NEW.value ->{
                slider.valueTo = 10F
                slider.value = 5F
            }
            StatProgress.PARTIALLY_LEARNED.value ->{
                slider.valueTo = 18F
                slider.value = 9F
            }
            StatProgress.ALMOST_LEARNED.value ->{
                slider.value = 0F
                slider.visibility = View.GONE
                textDialog.text = "Кажется вы уже запомнили эту карточку. Нажав \"OK\", вы перенесете эту карточку в группу \"изученные\"."
            }
        }

        btnYes.setOnClickListener {
            when(wordCard.progressWord?.statProgress){
                StatProgress.NEW.value ->{
                    wordCard.progressWord?.statProgress = StatProgress.PARTIALLY_LEARNED.value
                }
                StatProgress.PARTIALLY_LEARNED.value ->{
                    wordCard.progressWord?.statProgress = StatProgress.ALMOST_LEARNED.value
                }
                StatProgress.ALMOST_LEARNED.value ->{
                    wordCard.progressWord?.statProgress = StatProgress.LEARNED.value
                }
            }

            wordCard.progressWord?.numCorrAnsv = 0

                //вычисляем дату времени, до которой должна заснуть карточка
                val calendar = Calendar.getInstance()
                calendar.time = Date()
                calendar.add(Calendar.DAY_OF_MONTH, slider.value.toInt())
                val newDateString = SimpleDateFormat(SIM_FORM_DATE).format(calendar.time)
                println("Новая дата: $newDateString")
            wordCard.progressWord?.sleepTime = newDateString

            //отправляем все обновления в репозиторий
            viewModel.updateWordCard(wordCard)

            dialog.dismiss()
            adapter.notifyItemChanged(binding.viewPager2.currentItem)
            flippingCard()
        }
        btnCansel.setOnClickListener {
            dialog.dismiss()
            flippingCard()
        }
        dialog.show()
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

    /**
     * метод занимается показом количества карточек находящихся в обудении на данный момент
     */
    fun showCountCards() {
        val viewPager2 = binding.viewPager2

        countCards.text = "${viewPager2.currentItem + 1} / $countWordCards"

        binding.viewPager2.registerOnPageChangeCallback(object: ViewPager2.OnPageChangeCallback(){
            override fun onPageScrollStateChanged(state: Int) {
                super.onPageScrollStateChanged(state)
                if (state == 0&&adapter.isWordCard(viewPager2.currentItem)){
                    countCards.text = "${viewPager2.currentItem + 1} / $countWordCards"
                } else if (state == 0&&!adapter.isWordCard(viewPager2.currentItem)){
                    countCards.text = ""
                }
            }
        })
    }
}