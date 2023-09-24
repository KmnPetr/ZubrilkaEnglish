package com.example.zubrilkaenglish.screens.directoryWords.listlWords

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.example.zubrilkaenglish.R
import com.example.zubrilkaenglish.databinding.WordViewBinding
import com.example.zubrilkaenglish.models.Word
import com.example.zubrilkaenglish.models.WordCard
import com.example.zubrilkaenglish.utils.MYBUNDLE
import com.example.zubrilkaenglish.utils.StatProgress

class ListWordsAdapter(navController: NavController) : RecyclerView.Adapter<ListWordsAdapter.AllWordHolder>() {

    var listWords= emptyList<WordCard>()
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
        fun bind(wordCard: WordCard) {
            idWord = wordCard.word.id
            binding.foreignWord.text=wordCard.word.foreignWord
            binding.translation.text=wordCard.word.translation

            //добавим прозрачности
            val transpGray = Color.argb(128, Color.red(Color.GRAY), Color.green(Color.GRAY), Color.blue(Color.GRAY))
            val transpRed = Color.argb(128, Color.red(Color.RED), Color.green(Color.RED), Color.blue(Color.RED))
            val transpYellow = Color.argb(128, Color.red(Color.YELLOW), Color.green(Color.YELLOW), Color.blue(Color.YELLOW))
            val transpGreen = Color.argb(128, Color.red(Color.GREEN), Color.green(Color.GREEN), Color.blue(Color.GREEN))

            if (wordCard.progressWord==null){
                binding.linearLayout.setBackgroundColor(transpGray)
            }else if(wordCard.progressWord?.statProgress == StatProgress.NEW.value){
                val gradient = GradientDrawable(
                    GradientDrawable.Orientation.LEFT_RIGHT, intArrayOf(
                        transpRed, transpGray, transpGray, transpGray, transpGray, transpGray, transpGray))
                gradient.shape = GradientDrawable.RECTANGLE
                binding.linearLayout.background = gradient
            }else if(wordCard.progressWord?.statProgress == StatProgress.ALMOST_LEARNED.value||wordCard.progressWord?.statProgress == StatProgress.PARTIALLY_LEARNED.value){
                val gradient = GradientDrawable(
                    GradientDrawable.Orientation.LEFT_RIGHT, intArrayOf(
                        transpYellow, transpGray, transpGray, transpGray, transpGray, transpGray, transpGray))
                gradient.shape = GradientDrawable.RECTANGLE
                binding.linearLayout.background = gradient
            }else if(wordCard.progressWord?.statProgress == StatProgress.LEARNED.value){
                val gradient = GradientDrawable(
                    GradientDrawable.Orientation.LEFT_RIGHT, intArrayOf(
                        transpGreen, transpGray, transpGray, transpGray, transpGray, transpGray, transpGray))
                gradient.shape = GradientDrawable.RECTANGLE
                binding.linearLayout.background = gradient
            }
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

    public fun setList(list:List<WordCard>){
        listWords=list
        notifyDataSetChanged()
    }
}