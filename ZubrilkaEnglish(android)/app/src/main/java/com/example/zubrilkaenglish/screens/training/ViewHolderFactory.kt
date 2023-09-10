package com.example.zubrilkaenglish.screens.training

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.zubrilkaenglish.R
import com.example.zubrilkaenglish.databinding.NewsCardBinding
import com.example.zubrilkaenglish.databinding.WordCardBinding
import com.example.zubrilkaenglish.models.ICard
import com.example.zubrilkaenglish.models.NewsCard
import com.example.zubrilkaenglish.models.WordCard

class ViewHolderFactory {

    class WordCardHolder(item: View): RecyclerView.ViewHolder(item){
        val binding= WordCardBinding.bind(item)

        fun bind(wordCard: WordCard, listener: CardAdapter.Listener){
            binding.numCorrAnsv.text = "("+wordCard.progressWord.numCorrAnsv.toString()+")"
            binding.statusCard.text = "status: "+ wordCard.progressWord.statProgress
            binding.foreignWord.text = wordCard.word.foreignWord
            binding.transcription.text = wordCard.word.transcription
            binding.translation.text = wordCard.word.translation

            //блок if else решает проблему переиспользуемости холдеров
            if (wordCard.cardHasChanged){
                binding.translation.visibility=View.VISIBLE
                binding.yesButton.isEnabled=false
                binding.noButton.isEnabled=false
                binding.lookButton.isEnabled=false
            }else{
                binding.translation.visibility=View.INVISIBLE
                binding.yesButton.isEnabled=true
                binding.noButton.isEnabled=true
                binding.lookButton.isEnabled=true
                if (wordCard.lookButtonPressed/*проверка нажатости кнопки look*/){
                    binding.translation.visibility=View.VISIBLE
                    binding.lookButton.isEnabled=false
                }
            }

            binding.yesButton.setOnClickListener {
                listener.onClickYesButton(wordCard)
            }

            binding.noButton.setOnClickListener {
                listener.onClickNoButton(wordCard)
            }

            binding.lookButton.setOnClickListener {
                listener.onClickLookButton(wordCard)
            }
        }
    }
    class NewsCardHolder(item: View): RecyclerView.ViewHolder(item){
        val binding= NewsCardBinding.bind(item)

        fun bind(newsCard: NewsCard, listener: CardAdapter.Listener/*listener в будущем пригодится*/){
            binding.textNewsCard.text=newsCard.news
        }
    }

    companion object {
        fun create(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            when(viewType){
                ICard.WORD_CARD_TYPE ->{
                    val view= LayoutInflater.from(parent.context).inflate(R.layout.word_card,parent,false)
                    return WordCardHolder(view)
                }
                ICard.NEWS_CARD_TYPE ->{
                    val view= LayoutInflater.from(parent.context).inflate(R.layout.news_card,parent,false)
                    return NewsCardHolder(view)
                }
                else->{
                    return throw java.lang.IllegalStateException("Invalid rating param value")
                }
            }
        }
    }
}