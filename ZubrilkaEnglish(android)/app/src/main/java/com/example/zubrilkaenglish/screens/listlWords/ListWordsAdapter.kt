package com.example.zubrilkaenglish.screens.listlWords

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.example.zubrilkaenglish.R
import com.example.zubrilkaenglish.databinding.WordViewBinding
import com.example.zubrilkaenglish.models.Word
import com.example.zubrilkaenglish.utils.MYBUNDLE

class ListWordsAdapter(navController: NavController) : RecyclerView.Adapter<ListWordsAdapter.AllWordHolder>() {

    var listWords= emptyList<Word>()
    //navController нужен для перехода на другой фрагмент при нажатии на элемент списка
    private val navController = navController

    class AllWordHolder(view: View, navController: NavController) : RecyclerView.ViewHolder(view){
        private val binding=WordViewBinding.bind(view)
        private var idWord: Int? = null

        init {
            view.setOnClickListener {
                println("нажато слово с id: "+ idWord)
                MYBUNDLE["id_pressed_word"] = idWord!!
                navController.navigate(R.id.action_listWordsFragment_to_wordDetailsFragment)
            }
        }
        fun bind(word: Word) {
            idWord = word.id
            binding.foreignWord.text=word.foreignWord
            binding.translation.text=word.translation
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AllWordHolder {
        val view=LayoutInflater.from(parent.context).inflate(R.layout.word_view,parent,false)
        return AllWordHolder(view, navController)
    }

    override fun getItemCount(): Int {
        return listWords.size
    }

    override fun onBindViewHolder(holder: AllWordHolder, position: Int) {
        holder.bind(listWords[position])
    }

    public fun setList(list:List<Word>){
        listWords=list
        notifyDataSetChanged()
    }
}