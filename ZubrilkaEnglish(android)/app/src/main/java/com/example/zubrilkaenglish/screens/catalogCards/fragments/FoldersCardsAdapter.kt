package com.example.zubrilkaenglish.screens.catalogCards.fragments

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.zubrilkaenglish.R
import com.example.zubrilkaenglish.databinding.FolderViewBinding

class FoldersCardsAdapter(private val listener: FragmentItem): RecyclerView.Adapter<FoldersCardsAdapter.TopicCardHolder>() {

    private var listFolders = emptyList<String>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TopicCardHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.folder_view,parent,false)
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
        private val binding = FolderViewBinding.bind(view)

        fun bind(nameFolder: String, position: Int, listener: FragmentItem){
            binding.nameFolder.text = nameFolder

            binding.root.setOnClickListener {
                listener.onClickFolder(position)
            }
        }
    }
}