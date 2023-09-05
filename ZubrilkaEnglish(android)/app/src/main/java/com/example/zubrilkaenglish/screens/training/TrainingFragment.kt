package com.example.zubrilkaenglish.screens.training

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import com.example.zubrilkaenglish.databinding.FragmentTrainingBinding
import com.example.zubrilkaenglish.screens.training.anyfiles.CardAdapter
import com.example.zubrilkaenglish.screens.training.anyfiles.NewsCard
import com.example.zubrilkaenglish.screens.training.anyfiles.WordCard
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class TrainingFragment : Fragment(), CardAdapter.Listener {

    private lateinit var viewModel: TrainingViewModel
    private lateinit var binding: FragmentTrainingBinding
    private val adapter= CardAdapter(this)


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


        addCardToAdapter(adapter)

        binding.viewPager2.adapter=adapter

//        MyListener()

        binding.viewPager2.registerOnPageChangeCallback(object: ViewPager2.OnPageChangeCallback() {
            override fun onPageScrollStateChanged(state: Int) {
                super.onPageScrollStateChanged(state)
                s777 =0
            }
        })
    }

    /**
     * Функция заполняет cardList адаптера
     */
    fun addCardToAdapter(adapter: CardAdapter){
        for (index in 0..10)adapter.cardList.add(WordCard("Foreign Word $index","[transcription $index]","перевод $index"))
        adapter.cardList.add(NewsCard("news will be here"))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        /*посоветовали отменить регистрацию
        binding.pager.unregisterOnPageChangeCallback(this)*/
    }

    override fun onClickYesButton(wordCard: WordCard) {
        wordCard.cardHasChanged=true

        if (wordCard.cardProgress<100) {
            wordCard.cardProgress+=1
        }

        adapter.notifyItemChanged(binding.viewPager2.currentItem)

        /*перелистывание фрагмента на следующий
          с защитой от перелистывания во время скролла пальцем*/
        s777 =1
        GlobalScope.launch{
            delay(700L)
            if (s777 !=0) {
                s777 =0
                binding.viewPager2.setCurrentItem((binding.viewPager2.currentItem + 1),true)
            }
        }
    }

    override fun onClickNoButton(wordCard: WordCard) {
        wordCard.cardHasChanged=true

        if (wordCard.cardProgress>0) {
            wordCard.cardProgress-=1
        }
        adapter.notifyItemChanged(binding.viewPager2.currentItem)

        /*перелистывание фрагмента на следующий
          с защитой от перелистывания во время скролла пальцем*/
        s777 =1
        GlobalScope.launch{
            delay(700L)
            if (s777 !=0) {
                s777 =0
                binding.viewPager2.setCurrentItem((binding.viewPager2.currentItem + 1),true)
            }
        }
    }

    override fun onClickLookButton(wordCard: WordCard) {
        wordCard.lookButtonPressed=true
        adapter.notifyItemChanged(binding.viewPager2.currentItem)
    }
}

//служебная переменная используемая для защиты от перелистывания во время скролла пальцем
var s777:Int=0