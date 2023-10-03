package com.example.zubrilkaenglish.screens.catalogCards.fragments.userCardsFragment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.zubrilkaenglish.R
import com.example.zubrilkaenglish.databinding.FolderViewBinding

class UserCardsAdapter(): RecyclerView.Adapter<UserCardsAdapter.UserCardsHolder>() {

    private var listFolders = emptyList<String>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserCardsHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.folder_view,parent,false)
        return UserCardsHolder(view)
    }

    override fun onBindViewHolder(holder: UserCardsHolder, position: Int) {
        holder.bind(listFolders[position])
    }

    override fun getItemCount(): Int {
        return listFolders.size
    }

    fun setList(listNamesFolders: List<String>){
        this.listFolders = listNamesFolders
        notifyDataSetChanged()
    }

    class UserCardsHolder(view: View): RecyclerView.ViewHolder(view){
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