package com.example.zubrilkaenglish.screens.memo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.zubrilkaenglish.R
import com.example.zubrilkaenglish.databinding.ViewMemoBinding
import com.example.zubrilkaenglish.events.MemoEvent
import com.example.zubrilkaenglish.events.MmEvEnum
import com.example.zubrilkaenglish.models.DayOfWeek
import com.example.zubrilkaenglish.models.Memo
import org.greenrobot.eventbus.EventBus

class MemoAdapter: RecyclerView.Adapter<MemoAdapter.MemoViewHolder>() {
    private var listMemo:List<Memo> = listOf()
    class MemoViewHolder(view:View): RecyclerView.ViewHolder(view) {
        private val binding = ViewMemoBinding.bind(view)
        fun bind(memo: Memo) {
            val minutes = if (memo.minutes<10) "0"+memo.minutes else memo.minutes
            binding.time.setText(memo.hour.toString()+":"+minutes)
            binding.daysOfWeek.text = fun():String{

                //сортанем список по позиции в энам классе
                val enumIndexMap = DayOfWeek.values().mapIndexed { index, dayOfWeek -> dayOfWeek to index }.toMap()
                val sortedList = memo.daysOfWeek.sortedBy { enumIndexMap[it] }

                val stringBuilder = StringBuilder()
                sortedList.forEach {
                    if (!stringBuilder.isEmpty()) stringBuilder.append(", ")
                    stringBuilder.append(it.ruStr)
                }
                return stringBuilder.toString()
            }()

            binding.note.text = "\t"+memo.note
            binding.deleteButton.setOnClickListener {
                EventBus.getDefault().post(MemoEvent(MmEvEnum.DELETE_MEMO,memo))
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MemoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.view_memo,parent,false)
        return MemoViewHolder(view)
    }

    override fun getItemCount(): Int = listMemo.size
    override fun onBindViewHolder(holder: MemoViewHolder, position: Int) {
        holder.bind(listMemo[position])
    }

    /**
     * установит новый список
     * перерисует адаптер
     */
    fun setList(listMemo: List<Memo>){
        this.listMemo = listMemo
        notifyDataSetChanged()
    }
}