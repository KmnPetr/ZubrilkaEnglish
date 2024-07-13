package com.example.zubrilkaenglish.screens.rating

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.zubrilkaenglish.R
import com.example.zubrilkaenglish.databinding.FragmentRatingBinding
import com.example.zubrilkaenglish.models.StatisticsDTO
import com.example.zubrilkaenglish.repositories.StatisticsRepository
import com.example.zubrilkaenglish.utils.LOG
import kotlin.math.absoluteValue

class RatingFragment : Fragment() {

    private lateinit var viewModel: RatingViewModel
    private lateinit var binding: FragmentRatingBinding
    private lateinit var recycler: RecyclerView
    private lateinit var adapter: StatisticsAdapter
    private lateinit var layoutManager:LinearLayoutManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRatingBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(RatingViewModel::class.java)

        recycler = binding.recycler
        layoutManager = LinearLayoutManager(requireContext())
        recycler.layoutManager = layoutManager
        adapter = StatisticsAdapter()
        recycler.adapter = adapter

        setListeners()
        listenOwnPosition()
    }

    override fun onStart() {
        StatisticsRepository.instance.getStatFirst1500()
        super.onStart()
    }

    override fun onStop() {
        StatisticsRepository.instance.clearStatList()
        super.onStop()
    }

    /**
     * будет прослушивать положение основной карточки статистики пользователя и предпринимать определеннные действия
     */
    private fun listenOwnPosition() {

        showOwnAddStat()

        recycler.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                showOwnAddStat()
            }
        })
    }

    /**
     * покажет дополнительное поле статистики основного пользователя
     * в зависимости от его видимости в списке прокрутки статистики
     */
    private fun showOwnAddStat() {
        when(isOwnStatVisible()){
            -1 -> {
                binding.ownAddPlace.visibility = View.VISIBLE
                binding.topEmptyStatAdd.visibility = View.GONE
                binding.bottomEmptyStatAdd.visibility = View.VISIBLE
                (binding.ownAddPlace.layoutParams as ConstraintLayout.LayoutParams).verticalBias = 0.0f
            }
            0 -> {
                binding.ownAddPlace.visibility = View.GONE
            }
            1 -> {
                binding.ownAddPlace.visibility = View.VISIBLE
                binding.topEmptyStatAdd.visibility = View.VISIBLE
                binding.bottomEmptyStatAdd.visibility = View.GONE
                (binding.ownAddPlace.layoutParams as ConstraintLayout.LayoutParams).verticalBias = 1.0f
            }
        }
    }

    /**
     * выяснит насколько видна карточка статистики основного пользователя
     * вернет -1 если карточка основного пользователя находиться выше видимых карточек
     * вернет 0 если карточка статистики видима
     * вернет 1 если она ниже видимых карточек
     */
    private fun isOwnStatVisible(): Int {
        val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
        val lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition()

        val positionToCheck = viewModel.ownPosition

        if (positionToCheck != null) {
            if (positionToCheck<firstVisibleItemPosition) return -1
            else if (positionToCheck in firstVisibleItemPosition..lastVisibleItemPosition) return 0
            else if (positionToCheck>lastVisibleItemPosition) return 1
            else return 0
        } else{
            Log.d(LOG,"positionToCheck == null")
            return 0
        }
    }

    /**
     * установит различные слушатели
     */
    private fun setListeners() {
        viewModel.statistics.observe(viewLifecycleOwner){
            showOwnAddStat()
            if (it != null) {
                adapter.setListAndOwnId(it,viewModel.ownStats?.personId)
                bindOwnAddStat(viewModel.ownStats)
            } else adapter.setListAndOwnId(emptyList(),null)
        }
    }

    /**
     * заполнит поля дополнительной карточки основного пользователя
     */
    private fun bindOwnAddStat(stat: StatisticsDTO?) {
        if (stat!=null){
            binding.ownAddPlace.visibility = View.VISIBLE

            binding.place.setTextColor(Color.parseColor("#5A9E2F"))
            binding.name.setTextColor(Color.parseColor("#5A9E2F"))
            binding.points.setTextColor(Color.parseColor("#5A9E2F"))
            binding.lastEntry.setTextColor(Color.parseColor("#5A9E2F"))
            binding.place.setTypeface(null, Typeface.BOLD_ITALIC)
            binding.name.setTypeface(null, Typeface.BOLD_ITALIC)
            binding.points.setTypeface(null, Typeface.BOLD_ITALIC)
            binding.lastEntry.setTypeface(null, Typeface.BOLD_ITALIC)

            if (stat.place == 1){
                binding.place.visibility = View.GONE
                binding.placeImage.visibility = View.VISIBLE
                binding.placeImage.setImageResource(R.drawable.place1th)
                binding.ownStatAdd.setBackgroundColor(Color.parseColor("#FFEE96"))
            }else if (stat.place == 2){
                binding.place.visibility = View.GONE
                binding.placeImage.visibility = View.VISIBLE
                binding.placeImage.setImageResource(R.drawable.place2th)
                binding.ownStatAdd.setBackgroundColor(Color.parseColor("#FFEE96"))
            }else if (stat.place == 3){
                binding.place.visibility = View.GONE
                binding.placeImage.visibility = View.VISIBLE
                binding.placeImage.setImageResource(R.drawable.place3th)
                binding.ownStatAdd.setBackgroundColor(Color.parseColor("#FFEE96"))
            }else if (stat.place in 4..10){
                binding.place.visibility = View.VISIBLE
                binding.placeImage.visibility = View.GONE
                binding.place.text = stat.place.toString()
                binding.ownStatAdd.setBackgroundColor(Color.parseColor("#FFEE96"))
            }else if (stat.place in 11..100){
                binding.place.visibility = View.VISIBLE
                binding.placeImage.visibility = View.GONE
                binding.place.text = stat.place.toString()
                binding.ownStatAdd.setBackgroundColor(Color.parseColor("#A2DCFF"))
            }else if (stat.place in 101..1000){
                binding.place.visibility = View.VISIBLE
                binding.placeImage.visibility = View.GONE
                binding.place.text = stat.place.toString()
                binding.ownStatAdd.setBackgroundColor(Color.WHITE)
            }else if(stat.place>1000){
                binding.place.visibility = View.VISIBLE
                binding.placeImage.visibility = View.GONE
                binding.place.text = "  "
                binding.ownStatAdd.setBackgroundColor(Color.WHITE)
            }

            binding.name.text = stat.short_name.toString()
            binding.newPoints.text = stat.newPoints.absoluteValue.toString()
            binding.points.text = stat.points.toString()
            binding.lastEntry.text = stat.lastEntry

            if (stat.newPoints<0){
                binding.arrow.visibility = View.VISIBLE
                binding.arrow.setImageResource(android.R.drawable.arrow_down_float)
                binding.arrow.setColorFilter(Color.RED)
                binding.newPoints.setTextColor(Color.RED)
            } else if (stat.newPoints ==0){
                binding.arrow.visibility = View.INVISIBLE
                binding.newPoints.visibility = View.INVISIBLE
            }else if (stat.newPoints>0){
                binding.arrow.visibility = View.VISIBLE
                binding.arrow.setImageResource(android.R.drawable.arrow_up_float)
                binding.arrow.setColorFilter(Color.parseColor("#36750E"))
                binding.newPoints.setTextColor(Color.parseColor("#36750E"))
            }
        } else {
            binding.ownAddPlace.visibility = View.GONE
            binding.bottomEmptyStatAdd.visibility = View.GONE
            binding.topEmptyStatAdd.visibility = View.GONE
        }
    }

}