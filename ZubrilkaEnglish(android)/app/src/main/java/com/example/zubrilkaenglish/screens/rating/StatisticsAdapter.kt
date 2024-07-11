package com.example.zubrilkaenglish.screens.rating

import android.graphics.Color
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.zubrilkaenglish.R
import com.example.zubrilkaenglish.databinding.ViewStatisticsBinding
import com.example.zubrilkaenglish.models.StatisticsDTO

/**
 * адаптер списка статистики юзеров этоко приложения
 */
class StatisticsAdapter: RecyclerView.Adapter<StatisticsAdapter.StatisticsViewHolder>() {

    private var list:List<StatisticsDTO>  = emptyList()
    private var ownId:Long? = null

    class StatisticsViewHolder(view: View):RecyclerView.ViewHolder(view){
        val binding = ViewStatisticsBinding.bind(view)
        fun bind(stat: StatisticsDTO, ownId: Long?){

            if (stat.personId==ownId){
                binding.place.setTextColor(Color.parseColor("#5A9E2F"))
                binding.name.setTextColor(Color.parseColor("#5A9E2F"))
                binding.points.setTextColor(Color.parseColor("#5A9E2F"))
                binding.lastEntry.setTextColor(Color.parseColor("#5A9E2F"))
                binding.place.setTypeface(null,Typeface.BOLD_ITALIC)
                binding.name.setTypeface(null,Typeface.BOLD_ITALIC)
                binding.points.setTypeface(null,Typeface.BOLD_ITALIC)
                binding.lastEntry.setTypeface(null,Typeface.BOLD_ITALIC)
            } else {
                binding.place.setTextColor(Color.BLACK)
                binding.name.setTextColor(Color.BLACK)
                binding.points.setTextColor(Color.BLACK)
                binding.lastEntry.setTextColor(Color.BLACK)
                binding.place.setTypeface(null,Typeface.BOLD)
                binding.name.setTypeface(null,Typeface.NORMAL)
                binding.points.setTypeface(null,Typeface.NORMAL)
                binding.lastEntry.setTypeface(null,Typeface.NORMAL)
            }

            if (stat.place == 1){
                binding.place.visibility = View.GONE
                binding.placeImage.visibility = View.VISIBLE
                binding.placeImage.setImageResource(R.drawable.place1th)
                binding.linearLayout.setBackgroundColor(Color.parseColor("#FFEE96"))
            }else if (stat.place == 2){
                binding.place.visibility = View.GONE
                binding.placeImage.visibility = View.VISIBLE
                binding.placeImage.setImageResource(R.drawable.place2th)
                binding.linearLayout.setBackgroundColor(Color.parseColor("#FFEE96"))
            }else if (stat.place == 3){
                binding.place.visibility = View.GONE
                binding.placeImage.visibility = View.VISIBLE
                binding.placeImage.setImageResource(R.drawable.place3th)
                binding.linearLayout.setBackgroundColor(Color.parseColor("#FFEE96"))
            }else if (stat.place in 4..10){
                binding.place.visibility = View.VISIBLE
                binding.placeImage.visibility = View.GONE
                binding.place.text = stat.place.toString()
                binding.linearLayout.setBackgroundColor(Color.parseColor("#FFEE96"))
            }else if (stat.place in 11..100){
                binding.place.visibility = View.VISIBLE
                binding.placeImage.visibility = View.GONE
                binding.place.text = stat.place.toString()
                binding.linearLayout.setBackgroundColor(Color.parseColor("#A2DCFF"))
            }else if (stat.place in 101..1000){
                binding.place.visibility = View.VISIBLE
                binding.placeImage.visibility = View.GONE
                binding.place.text = stat.place.toString()
                binding.linearLayout.setBackgroundColor(Color.WHITE)
            }else if(stat.place>1000){
                binding.place.visibility = View.VISIBLE
                binding.placeImage.visibility = View.GONE
                binding.place.text = "  "
                binding.linearLayout.setBackgroundColor(Color.WHITE)
            }

            binding.name.text = stat.short_name.toString()
            binding.newPoints.text = stat.newPoints.toString()
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
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StatisticsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.view_statistics,parent,false)
        return StatisticsViewHolder(view)
    }

    override fun onBindViewHolder(holder: StatisticsViewHolder, position: Int) {
        holder.bind(list[position],ownId)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun setListAndOwnId(list:List<StatisticsDTO>,ownId:Long?){
        println("setList SIZE: ${list.size}")
        this.list = list
        this.ownId = ownId
        notifyDataSetChanged()
    }
}