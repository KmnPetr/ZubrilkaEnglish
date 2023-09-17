package com.example.zubrilkaenglish.screens.myCards.listCards

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.example.zubrilkaenglish.R
import com.example.zubrilkaenglish.databinding.WordViewBinding
import com.example.zubrilkaenglish.models.WordCard
import com.example.zubrilkaenglish.utils.MYBUNDLE

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
        fun bind(card: WordCard) {
            idWord = card.word.id
            binding.foreignWord.text=card.word.foreignWord
            binding.translation.text=card.word.translation
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