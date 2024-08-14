package com.zubrilka.zubrilkaenglish.screens.catalogCards.fragments

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.zubrilka.zubrilkaenglish.R
import com.zubrilka.zubrilkaenglish.databinding.ViewWordBinding
import com.zubrilka.zubrilkaenglish.models.WordCard
import com.zubrilka.zubrilkaenglish.utils.StatProgress

/**
 * адаптер занимается показом списка карточек (слов, фраз)
 */
class ListCardsAdapter(val fragmentItem: FragmentItem) : RecyclerView.Adapter<ListCardsAdapter.CardHolder>() {

    private var listCards= emptyList<WordCard>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardHolder {
        val view= LayoutInflater.from(parent.context).inflate(R.layout.view_word,parent,false)
        return CardHolder(view)
    }

    override fun getItemCount(): Int {
        return listCards.size
    }

    override fun onBindViewHolder(holder: CardHolder, position: Int) {
        holder.bind(listCards[position], fragmentItem,position)
    }

    fun setList(list:List<WordCard>){
        listCards=list
        notifyDataSetChanged()
    }
    class CardHolder(view: View) : RecyclerView.ViewHolder(view){
        private val binding= ViewWordBinding.bind(view)
        private var idWord: Int? = null

        fun bind(wordCard: WordCard, fragmentItem: FragmentItem,position: Int) {
            idWord = wordCard.word.id
            binding.foreignWord.text=wordCard.word.foreignWord
            binding.translation.text=wordCard.word.translation

            binding.linearLayout.setCardBackgroundColor(setUpBackground(wordCard))

            binding.root.setOnClickListener {
                fragmentItem.owner.onClickCard(wordCard,position)
            }
        }

        /**
         * функция настроит цвет и прозрачность элемента карточки исходя из ее статуса выученности юзером
         */
        private fun setUpBackground(wordCard: WordCard): Int {
            //добавим прозрачности
            val transp : Int = 50
            val transpGray = Color.argb(transp, Color.red(Color.GRAY), Color.green(Color.GRAY), Color.blue(
                Color.GRAY))
            val transpRed = Color.argb(transp, Color.red(Color.RED), Color.green(Color.RED), Color.blue(
                Color.RED))
            val transpYellow = Color.argb(transp, Color.red(Color.YELLOW), Color.green(Color.YELLOW), Color.blue(
                Color.YELLOW))
            val transpGreen = Color.argb(transp, Color.red(Color.GREEN), Color.green(Color.GREEN), Color.blue(
                Color.GREEN))



            if (wordCard.progressWord==null){
                return transpGray
            }else if(wordCard.progressWord?.statProgress == StatProgress.NEW.value){
                return transpRed
            }else if(wordCard.progressWord?.statProgress == StatProgress.ALMOST_LEARNED.value||wordCard.progressWord?.statProgress == StatProgress.PARTIALLY_LEARNED.value){
                return transpYellow
            }else if(wordCard.progressWord?.statProgress == StatProgress.LEARNED.value){
                return transpGreen
            }else {
                return transpGray
            }

            /**
             * старый код,
             * делает типа, чтобы карточка была слева красная а справа серая
             * Не получилось обьект GradientDrawable установить фоном для CardView
             * оставлю, может потом что придумаю
             */
            //if (wordCard.progressWord==null){
            //    this.itemView.setBackgroundColor(transpGray)
            //}else if(wordCard.progressWord?.statProgress == StatProgress.NEW.value){
            //    val gradient = GradientDrawable(
            //        GradientDrawable.Orientation.LEFT_RIGHT, intArrayOf(
            //            transpRed, transpGray, transpGray, transpGray, transpGray, transpGray, transpGray))
            //    gradient.shape = GradientDrawable.RECTANGLE
            //    this.itemView.background = gradient
            //}else if(wordCard.progressWord?.statProgress == StatProgress.ALMOST_LEARNED.value||wordCard.progressWord?.statProgress == StatProgress.PARTIALLY_LEARNED.value){
            //    val gradient = GradientDrawable(
            //        GradientDrawable.Orientation.LEFT_RIGHT, intArrayOf(
            //            transpYellow, transpGray, transpGray, transpGray, transpGray, transpGray, transpGray))
            //    gradient.shape = GradientDrawable.RECTANGLE
            //    this.itemView.background = gradient
            //}else if(wordCard.progressWord?.statProgress == StatProgress.LEARNED.value){
            //    val gradient = GradientDrawable(
            //        GradientDrawable.Orientation.LEFT_RIGHT, intArrayOf(
            //            transpGreen, transpGray, transpGray, transpGray, transpGray, transpGray, transpGray))
            //    gradient.shape = GradientDrawable.RECTANGLE
            //    this.itemView.background = gradient
            //}
        }
    }
}