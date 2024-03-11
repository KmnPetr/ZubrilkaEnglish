package com.example.ze_adminandroid.screens.catalogWords.fragments

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ze_adminandroid.R
import com.example.ze_adminandroid.databinding.ViewFolderBinding

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