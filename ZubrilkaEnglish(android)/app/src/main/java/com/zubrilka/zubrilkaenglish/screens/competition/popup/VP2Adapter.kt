package com.zubrilka.zubrilkaenglish.screens.competition.popup

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

/**
 * перелистывается между фрагментами
 * например межу фрагментов ошибочно выбранных слов и правильно выбранных
 */
class VP2Adapter(private val context: Context, private val recyclerViewAdapters: List<RecyclerView.Adapter<*>>) :
    RecyclerView.Adapter<VP2Adapter.ViewPagerViewHolder>() {

    class ViewPagerViewHolder(val recyclerView: RecyclerView) : RecyclerView.ViewHolder(recyclerView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewPagerViewHolder {
        val recyclerView = RecyclerView(context)
        recyclerView.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        recyclerView.layoutManager = LinearLayoutManager(context)
        return ViewPagerViewHolder(recyclerView)
    }

    override fun onBindViewHolder(holder: ViewPagerViewHolder, position: Int) {
        holder.recyclerView.adapter = recyclerViewAdapters[position]
    }

    override fun getItemCount(): Int {
        return recyclerViewAdapters.size
    }
}