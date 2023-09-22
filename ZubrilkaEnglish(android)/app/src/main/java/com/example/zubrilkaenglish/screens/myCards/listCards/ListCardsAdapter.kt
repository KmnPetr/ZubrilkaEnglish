package com.example.zubrilkaenglish.screens.myCards.listCards

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.example.zubrilkaenglish.R
import com.example.zubrilkaenglish.databinding.WordViewBinding
import com.example.zubrilkaenglish.models.WordCard
import com.example.zubrilkaenglish.utils.MYBUNDLE
import com.example.zubrilkaenglish.utils.StatProgress

class ListCardsAdapter(navController: NavController) : RecyclerView.Adapter<ListCardsAdapter.CardHolder>() {

    var listCards= emptyList<WordCard>()
    //navController нужен для перехода на другой фрагмент при нажатии на элемент списка
    private val navController = navController

    class CardHolder(view: View, navController: NavController) : RecyclerView.ViewHolder(view){
        private val binding= WordViewBinding.bind(view)
        private var idWord: Int? = null

        init {
            view.setOnClickListener {
                MYBUNDLE["id_pressed_word"] = idWord!!
                navController.navigate(R.id.action_listCardsFragment_to_wordDetailsFragment)
            }
        }
        fun bind(wordCard: WordCard) {
            idWord = wordCard.word.id
            binding.foreignWord.text=wordCard.word.foreignWord
            binding.translation.text=wordCard.word.translation


            if (wordCard.progressWord==null){
                binding.linearLayout.setBackgroundColor(Color.GRAY)
            }else if(wordCard.progressWord?.statProgress == StatProgress.NEW.value){
                val gradient = GradientDrawable(
                    GradientDrawable.Orientation.LEFT_RIGHT, intArrayOf(
                        Color.RED, Color.GRAY, Color.GRAY, Color.GRAY, Color.GRAY, Color.GRAY, Color.GRAY))
                gradient.shape = GradientDrawable.RECTANGLE
                binding.linearLayout.background = gradient
            }else if(wordCard.progressWord?.statProgress == StatProgress.ALMOST_LEARNED.value||wordCard.progressWord?.statProgress == StatProgress.PARTIALLY_LEARNED.value){
                val gradient = GradientDrawable(
                    GradientDrawable.Orientation.LEFT_RIGHT, intArrayOf(
                        Color.YELLOW, Color.GRAY, Color.GRAY, Color.GRAY, Color.GRAY, Color.GRAY, Color.GRAY))
                gradient.shape = GradientDrawable.RECTANGLE
                binding.linearLayout.background = gradient
            }else if(wordCard.progressWord?.statProgress == StatProgress.LEARNED.value){
                val gradient = GradientDrawable(
                    GradientDrawable.Orientation.LEFT_RIGHT, intArrayOf(
                        Color.GREEN, Color.GRAY, Color.GRAY, Color.GRAY, Color.GRAY, Color.GRAY, Color.GRAY))
                gradient.shape = GradientDrawable.RECTANGLE
                binding.linearLayout.background = gradient
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardHolder {
        val view= LayoutInflater.from(parent.context).inflate(R.layout.word_view,parent,false)
        return CardHolder(view, navController)
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