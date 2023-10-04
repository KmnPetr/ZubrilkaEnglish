package com.example.zubrilkaenglish.screens.catalogCards.fragments

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.zubrilkaenglish.R
import com.example.zubrilkaenglish.databinding.WordViewBinding
import com.example.zubrilkaenglish.models.WordCard
import com.example.zubrilkaenglish.utils.StatProgress

class ListCardsAdapter() : RecyclerView.Adapter<ListCardsAdapter.CardHolder>() {

    private var listCards= emptyList<WordCard>()

    class CardHolder(view: View) : RecyclerView.ViewHolder(view){
        private val binding= WordViewBinding.bind(view)
        private var idWord: Int? = null

        fun bind(wordCard: WordCard) {
            idWord = wordCard.word.id
            binding.foreignWord.text=wordCard.word.foreignWord
            binding.translation.text=wordCard.word.translation

            //добавим прозрачности
            val transpGray = Color.argb(128, Color.red(Color.GRAY), Color.green(Color.GRAY), Color.blue(
                Color.GRAY))
            val transpRed = Color.argb(128, Color.red(Color.RED), Color.green(Color.RED), Color.blue(
                Color.RED))
            val transpYellow = Color.argb(128, Color.red(Color.YELLOW), Color.green(Color.YELLOW), Color.blue(
                Color.YELLOW))
            val transpGreen = Color.argb(128, Color.red(Color.GREEN), Color.green(Color.GREEN), Color.blue(
                Color.GREEN))

            if (wordCard.progressWord==null){
                this.itemView.setBackgroundColor(transpGray)
            }else if(wordCard.progressWord?.statProgress == StatProgress.NEW.value){
                val gradient = GradientDrawable(
                    GradientDrawable.Orientation.LEFT_RIGHT, intArrayOf(
                        transpRed, transpGray, transpGray, transpGray, transpGray, transpGray, transpGray))
                gradient.shape = GradientDrawable.RECTANGLE
                this.itemView.background = gradient
            }else if(wordCard.progressWord?.statProgress == StatProgress.ALMOST_LEARNED.value||wordCard.progressWord?.statProgress == StatProgress.PARTIALLY_LEARNED.value){
                val gradient = GradientDrawable(
                    GradientDrawable.Orientation.LEFT_RIGHT, intArrayOf(
                        transpYellow, transpGray, transpGray, transpGray, transpGray, transpGray, transpGray))
                gradient.shape = GradientDrawable.RECTANGLE
                this.itemView.background = gradient
            }else if(wordCard.progressWord?.statProgress == StatProgress.LEARNED.value){
                val gradient = GradientDrawable(
                    GradientDrawable.Orientation.LEFT_RIGHT, intArrayOf(
                        transpGreen, transpGray, transpGray, transpGray, transpGray, transpGray, transpGray))
                gradient.shape = GradientDrawable.RECTANGLE
                this.itemView.background = gradient
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardHolder {
        val view= LayoutInflater.from(parent.context).inflate(R.layout.word_view,parent,false)
        return CardHolder(view)
    }

    override fun getItemCount(): Int {
        return listCards.size
    }

    override fun onBindViewHolder(holder: CardHolder, position: Int) {
        holder.bind(listCards[position])
    }

    fun setList(list:List<WordCard>){
        listCards=list
        notifyDataSetChanged()
    }
}