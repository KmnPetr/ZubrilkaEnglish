package com.example.zubrilkaenglish.screens.training.popup

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.viewpager2.widget.ViewPager2
import com.example.zubrilkaenglish.databinding.PopupFinishInfoCompetitionBinding
import com.example.zubrilkaenglish.events.CmpEvEnum
import com.example.zubrilkaenglish.events.CompetitionEvent
import com.example.zubrilkaenglish.events.NfEvEnum
import com.example.zubrilkaenglish.events.NotificationEvent
import com.example.zubrilkaenglish.models.WordCard
import com.example.zubrilkaenglish.models.socketDto.FinishInfo
import com.example.zubrilkaenglish.repositories.CardsRepository
import com.example.zubrilkaenglish.screens.catalogCards.fragments.PopupWordCard
import com.example.zubrilkaenglish.screens.competition.CompetitionViewModel
import com.example.zubrilkaenglish.screens.competition.popup.CustomRecyclerViewAdapter
import com.example.zubrilkaenglish.screens.competition.popup.VP2Adapter
import com.google.android.material.tabs.TabLayout
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.EventBus

/**
 * в обязанности класса входит
 * показ всплывающего popup диалогового окошка
 * с различной инвормацией о завершении поединка между игроками
 */
class PopupFinishInfo(
    context: Context,
    private val finishInfo: FinishInfo,
    private val viewModel: CompetitionViewModel
) : Dialog(context) {
    private var binding = PopupFinishInfoCompetitionBinding.inflate(layoutInflater)
    private lateinit var viewPager2: ViewPager2
    private lateinit var tabLayout: TabLayout
    private lateinit var adapter: VP2Adapter
    private val cardsRepository = CardsRepository.instance

    init {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setCancelable(false)
        setContentView(binding.root)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        window?.setLayout(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )

        showResults()
        setupListWords()
        settingsView()
        setListeners()
    }

    /**
     * покажет результаты, имена игроков, очки
     */
    private fun showResults() {

        var opponentPos: Int? = null
        if (finishInfo.ownPos==0)opponentPos = 1
        else if (finishInfo.ownPos==1) opponentPos = 0

        if (opponentPos!=null){
            if (finishInfo.resultPoints.get(finishInfo.ownPos)>finishInfo.resultPoints.get(opponentPos)){ //победа
                binding.result.text = "Победа!"
                binding.result.setTextColor(Color.parseColor("#FF64DD17"))
            }else if (finishInfo.resultPoints.get(finishInfo.ownPos)<finishInfo.resultPoints.get(opponentPos)){ //проигр
                binding.result.text = "Поражение!"
                binding.result.setTextColor(Color.parseColor("#FFA11717"))
            }else if (finishInfo.resultPoints.get(finishInfo.ownPos)==finishInfo.resultPoints.get(opponentPos)){ //ничья
                binding.result.text = "Ничья!"
                binding.result.setTextColor(Color.parseColor("#00B8D4"))
            }
        }

        //покажем имена игроков
        binding.ownName.text = finishInfo.playersNames.get(finishInfo.ownPos)
        binding.opponentName.text = opponentPos?.let { finishInfo.playersNames.get(it) }
        //выставим значения очков и покрасим их
        if (finishInfo.resultPoints.get(finishInfo.ownPos)>0){
            binding.ownPoints.text = "+"+finishInfo.resultPoints.get(finishInfo.ownPos).toString()
            binding.ownPoints.setTextColor(Color.GREEN)
        }else if (finishInfo.resultPoints.get(finishInfo.ownPos)==0){
            binding.ownPoints.text = finishInfo.resultPoints.get(finishInfo.ownPos).toString()
            binding.ownPoints.setTextColor(Color.YELLOW)
        }else if (finishInfo.resultPoints.get(finishInfo.ownPos)<0){
            binding.ownPoints.text = finishInfo.resultPoints.get(finishInfo.ownPos).toString()
            binding.ownPoints.setTextColor(Color.RED)
        }

        if (opponentPos!=null){
            if (finishInfo.resultPoints.get(opponentPos)>0){
                binding.opponentPoints.text = "+"+finishInfo.resultPoints.get(opponentPos).toString()
                binding.opponentPoints.setTextColor(Color.GREEN)
            }else if (finishInfo.resultPoints.get(opponentPos)==0){
                binding.opponentPoints.text = finishInfo.resultPoints.get(opponentPos).toString()
                binding.opponentPoints.setTextColor(Color.YELLOW)
            }else if (finishInfo.resultPoints.get(opponentPos)<0){
                binding.opponentPoints.text = finishInfo.resultPoints.get(opponentPos).toString()
                binding.opponentPoints.setTextColor(Color.RED)
            }
        }
    }

    /**
     * сложно
     * покажет адаптер адаптеров со списком слов ошибок и списком правильных слов
     */
    private fun setupListWords() {
        // Создайте адаптеры для каждого RecyclerView
        val adapter1 = CustomRecyclerViewAdapter(this,Color.parseColor("#FFBBBB"))
        val adapter2 = CustomRecyclerViewAdapter(this,Color.parseColor("#DBFFC4"))

        GlobalScope.launch {
            adapter1.setList(cardsRepository.getListWordCardsBiId(finishInfo.mistakes))
            adapter2.setList(cardsRepository.getListWordCardsBiId(finishInfo.correctAnswers))
        }

        // Создайте список из адаптеров
        val adapters = listOf(adapter1, adapter2)

        tabLayout = binding.tabLayout
        viewPager2 = binding.viewPager2
        adapter = VP2Adapter(context,adapters)
        viewPager2.adapter = adapter

        //следит за перелистывание вью пейджера
        viewPager2.registerOnPageChangeCallback(object: ViewPager2.OnPageChangeCallback()
        {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                tabLayout.getTabAt(position)?.select()
            }
        })

        //прослушивает нажатия на tabLayout
        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                viewPager2.currentItem = tab.position
            }
            override fun onTabUnselected(tab: TabLayout.Tab) {/*Выполняется, когда выбор снят с вкладки*/ }
            override fun onTabReselected(tab: TabLayout.Tab) {/*Выполняется, когда выбирается уже выбранная вкладка*/ }
        })

    }

    /**
     * настроит отображение различных элементов
     */
    private fun settingsView() {
        binding.numCorrAnsv.text = finishInfo.correctAnswers.size.toString()
        binding.numMistakes.text = finishInfo.mistakes.size.toString()
        if (finishInfo.correctAnswers==null||finishInfo.correctAnswers.size == 0) binding.buttonLookCorrectAnsw.visibility = View.GONE
        if (finishInfo.mistakes==null||finishInfo.mistakes.size == 0) binding.buttonLookMistakes.visibility = View.GONE
        binding.listWords.visibility = View.GONE
    }

    /**
     * покажет список ошибок и правильных ответов
     */
    private fun showListWords() {
        binding.buttonLookCorrectAnsw.visibility = View.GONE
        binding.buttonLookMistakes.visibility = View.GONE
        binding.listWords.visibility = View.VISIBLE
    }

    /**
     * функция установит слушатели на все кнопки диалогового окна
     */
    private fun setListeners() {
        binding.buttonBack.setOnClickListener {
            this.dismiss()
            viewModel.lockShowStartPopup = false
            EventBus.getDefault().post(CompetitionEvent(CmpEvEnum.CLOSE_SESSION))
            EventBus.getDefault().post(NotificationEvent("",NfEvEnum.GO_TO_UPSTACK))
        }
        binding.buttonNextRound.setOnClickListener {
            this.dismiss()
            viewModel.lockShowStartPopup = false
            EventBus.getDefault().post(CompetitionEvent(CmpEvEnum.SET_WAITING_STATUS))
        }
        binding.buttonLookMistakes.setOnClickListener {
            tabLayout.getTabAt(0)?.select()
            viewPager2.currentItem = 0
            showListWords() }
        binding.buttonLookCorrectAnsw.setOnClickListener {
            tabLayout.getTabAt(1)?.select()
            viewPager2.currentItem = 1
            showListWords() }
        binding.tableLeaders.setOnClickListener {
            this.dismiss()
            viewModel.lockShowStartPopup = false
            EventBus.getDefault().post(CompetitionEvent(CmpEvEnum.CLOSE_SESSION))
            EventBus.getDefault().post(NotificationEvent("",NfEvEnum.GO_TO_RATING))
        }
    }

    /**
     * показывает popup окошко при нажатии на элемент карточки
     */
    fun onClickCard(wordCard: WordCard, position: Int){
        PopupWordCard(context,wordCard,position).show()
    }
}