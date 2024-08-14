package com.zubrilka.zubrilkaenglish.screens.competition.popup

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.zubrilka.zubrilkaenglish.R
import com.zubrilka.zubrilkaenglish.databinding.ViewWord2Binding
import com.zubrilka.zubrilkaenglish.models.WordCard
import com.zubrilka.zubrilkaenglish.screens.training.popup.PopupFinishInfo

class CustomRecyclerViewAdapter(
    private val listener: PopupFinishInfo,
    private val background: Int
) : RecyclerView.Adapter<CustomRecyclerViewAdapter.ViewHolder>() {

    private var list:List<WordCard> = emptyList()

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = ViewWord2Binding.bind(view)

        fun bind(wordCard: WordCard, position: Int, background: Int, listener: PopupFinishInfo) {
            binding.foreignWord.text = wordCard.word.foreignWord
            binding.translation.text=wordCard.word.translation

            binding.linearLayout.setCardBackgroundColor(background)

            binding.root.setOnClickListener {
                listener.onClickCard(wordCard,position)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view= LayoutInflater.from(parent.context).inflate(R.layout.view_word2,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position],position,background,listener)
    }

    override fun getItemCount() = list.size

    fun setList(list: List<WordCard>){
        this.list = list
        notifyDataSetChanged()
    }
}