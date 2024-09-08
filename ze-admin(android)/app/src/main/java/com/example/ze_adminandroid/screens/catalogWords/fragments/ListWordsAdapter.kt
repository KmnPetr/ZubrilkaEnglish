package com.example.ze_adminandroid.screens.catalogWords.fragments

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ze_adminandroid.R
import com.example.ze_adminandroid.databinding.ViewWordBinding
import com.example.ze_adminandroid.models.Word

/**
 * адаптер занимается показом списка карточек (слов, фраз)
 */
class ListCardsAdapter(val listener: FragmentItem) : RecyclerView.Adapter<ListCardsAdapter.CardHolder>() {

    private var listCards= emptyList<Word>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardHolder {
        val view= LayoutInflater.from(parent.context).inflate(R.layout.view_word,parent,false)
        return CardHolder(view)
    }

    override fun getItemCount(): Int {
        return listCards.size
    }

    override fun onBindViewHolder(holder: CardHolder, position: Int) {
        holder.bind(listCards[position], listener,position,/*::notifyItemChanged,*/this)
    }

    fun setList(list:List<Word>){
        listCards=list
        notifyDataSetChanged()
    }
    class CardHolder(view: View) : RecyclerView.ViewHolder(view){
        private val binding= ViewWordBinding.bind(view)
        private var idWord: Int? = null

        fun bind(
            word: Word,
            listener: FragmentItem,
            position: Int,
//            notifyItemChanged: (Int) -> Unit,
            adapter: ListCardsAdapter
        ) {
            idWord = word.id
            binding.foreignWord.text=word.foreignWord
            binding.translation.text=word.translation
            binding.sortingValue.text = "sv: "+word.sorting_value.toString()

            if (!word.voiceVerified) binding.linearLayout.setCardBackgroundColor(setUpBackground(word))
            else binding.linearLayout.setCardBackgroundColor(Color.parseColor("#CCFFCC"))

            binding.root.setOnClickListener {
                listener.onClickWord(word)
            }

            //настройка кнопки Play
            if (word.link_voice!=null && !word.link_voice.equals("")){
                binding.buttonPlay.visibility = View.VISIBLE
                binding.buttonPlay.setOnClickListener {
                    listener.onClickButtonPlay(word)
//                    notifyItemChanged(position)
                    adapter.notifyItemChanged(position)
                }
            }else binding.buttonPlay.visibility = View.GONE

            //
            //настройка кнопки Delete
            if (word.localBaseId!=0){
                binding.buttonDelete.visibility = View.VISIBLE
                binding.buttonDelete.setOnClickListener {
                    listener.onClickButtonDelete(word)
                    adapter.notifyDataSetChanged()
                }
            }else binding.buttonDelete.visibility = View.GONE
        }


        /**
         * функция настроит цвет и прозрачность элемента карточки
         */
        private fun setUpBackground(word: Word): Int {
            //добавим прозрачности
            val transp : Int = 50
            return Color.argb(transp, Color.red(Color.GRAY), Color.green(Color.GRAY), Color.blue(Color.GRAY))
        }
    }
}