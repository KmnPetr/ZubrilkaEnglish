package com.example.zubrilkaenglish.screens.training

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.zubrilkaenglish.models.ICard
import com.example.zubrilkaenglish.models.NewsCard
import com.example.zubrilkaenglish.models.WordCard

class CardAdapter(val listener: Listener):RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    var cardList:ArrayList<ICard> = ArrayList()

    /**
     * библиотечная функция определяет тип класса, находящегося в коллекции
     * реализация определения типа вынесена в сам класс шаблон
     */
    override fun getItemViewType(position: Int): Int {
        return cardList[position].getItemViewType()
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    /**
     * сами классы холдеры и реализация метода сращивания классов шаблонов с их xml файлом
     * вынесены в класс фабрику ViewHolderFactory
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolderFactory.create(parent, viewType)
    }

    override fun getItemCount(): Int {
        return cardList.size
    }

    /**
     * реализация классов холдеров вынесено в класс фабрику ViewHolderFactory
     */
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ViewHolderFactory.WordCardHolder){
            holder.bind((cardList[position] as WordCard),listener,position)
        }
        else if (holder is ViewHolderFactory.NewsCardHolder){
            holder.bind((cardList[position] as NewsCard),listener)
        }
    }

    fun setList(listForTreining: ArrayList<ICard>) {
        cardList = listForTreining
        notifyDataSetChanged()
    }

    /**
     * функция даст сведения о классе обьекта из листа по позиции в листе
     */
    public fun isWordCard(position: Int): Boolean{
        return cardList[position] is WordCard
    }

    fun getCurrentCard(position: Int): ICard {
        return cardList[position]
    }


    interface Listener{
        fun onClickYesButton(wordCard: WordCard, position: Int)
        fun onClickNoButton(wordCard: WordCard, position: Int)
        fun onClickLookButton(wordCard: WordCard)
        fun onClickOptionsButton(wordCard: WordCard)
    }
}