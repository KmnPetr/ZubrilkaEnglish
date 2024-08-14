package com.zubrilka.zubrilkaenglish.screens.catalogCards.fragments

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.zubrilka.zubrilkaenglish.R
import com.zubrilka.zubrilkaenglish.databinding.ViewFolderBinding

class FoldersCardsAdapter(private val listener: FragmentItem): RecyclerView.Adapter<FoldersCardsAdapter.TopicCardHolder>() {

    private var listFolders = emptyList<String>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TopicCardHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.view_folder,parent,false)
        return TopicCardHolder(view)
    }

    override fun onBindViewHolder(holder: TopicCardHolder, position: Int) {
        holder.bind(listFolders[position],position,listener)
    }

    override fun getItemCount(): Int {
        return listFolders.size
    }

    fun setList(listNamesFolders: List<String>){
        this.listFolders = listNamesFolders
        notifyDataSetChanged()
    }

    class TopicCardHolder(view: View): RecyclerView.ViewHolder(view){
        private val binding = ViewFolderBinding.bind(view)

        fun bind(nameFolder: String, position: Int, listener: FragmentItem){
            binding.nameFolder.text = nameFolder

            binding.root.setOnClickListener {
                listener.onClickFolder(position)
            }
        }
    }
}