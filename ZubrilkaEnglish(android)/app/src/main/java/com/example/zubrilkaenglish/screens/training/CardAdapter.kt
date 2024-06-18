package com.example.zubrilkaenglish.screens.training

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.zubrilkaenglish.models.WordCard
import com.example.zubrilkaenglish.screens.training.additionalCards.FewCards
import com.example.zubrilkaenglish.screens.training.additionalCards.ReviewCard

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

        ViewHolderFactory.onBindViewHolder(holder,cardList[position],listener,position)
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
        fun onClickOptionsButton(wordCard: WordCard, position: Int)
        fun completeTraining()
        fun restartTraining()
    }
}