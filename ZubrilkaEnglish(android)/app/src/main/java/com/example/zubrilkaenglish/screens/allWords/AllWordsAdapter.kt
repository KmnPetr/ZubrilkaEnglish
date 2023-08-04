package com.example.zubrilkaenglish.screens.allWords

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.zubrilkaenglish.R
import com.example.zubrilkaenglish.databinding.WordViewBinding
import com.example.zubrilkaenglish.models.Word

class AllWordsAdapter : RecyclerView.Adapter<AllWordsAdapter.AllWordHolder>() {

    var listWords= emptyList<Word>()

    class AllWordHolder(view: View) : RecyclerView.ViewHolder(view){
        val binding=WordViewBinding.bind(view)
        fun bind(word: Word) {
            binding.foreignWord.text=word.foreignWord
            binding.translation.text=word.translation
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AllWordHolder {
        val view=LayoutInflater.from(parent.context).inflate(R.layout.word_view,parent,false)
        return AllWordHolder(view)
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