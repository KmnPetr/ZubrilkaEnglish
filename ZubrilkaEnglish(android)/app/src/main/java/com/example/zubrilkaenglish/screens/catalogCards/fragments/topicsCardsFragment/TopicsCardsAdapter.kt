package com.example.zubrilkaenglish.screens.catalogCards.fragments.topicsCardsFragment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.zubrilkaenglish.R
import com.example.zubrilkaenglish.databinding.FolderViewBinding

class TopicsCardsAdapter(): RecyclerView.Adapter<TopicsCardsAdapter.TopicCardHolder>() {

    private var listFolders = emptyList<String>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TopicCardHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.folder_view,parent,false)
        return TopicCardHolder(view)
    }

    override fun onBindViewHolder(holder: TopicCardHolder, position: Int) {
        holder.bind(listFolders[position])
    }

    override fun getItemCount(): Int {
        return listFolders.size
    }

    fun setList(listNamesFolders: List<String>){
        this.listFolders = listNamesFolders
        notifyDataSetChanged()
    }

    class TopicCardHolder(view: View): RecyclerView.ViewHolder(view){
        val binding = FolderViewBinding.bind(view)

        init {
            view.setOnClickListener {
//                println("Нажат элемент: "+ binding.nameFolder.text)
            }
        }

        fun bind(nameFolder: String){
            binding.nameFolder.text = nameFolder
        }
    }
}