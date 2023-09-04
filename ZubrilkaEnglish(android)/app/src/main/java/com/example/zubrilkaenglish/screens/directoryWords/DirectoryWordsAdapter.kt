package com.example.zubrilkaenglish.screens.directoryWords

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import androidx.navigation.NavController
import com.example.zubrilkaenglish.R
import com.example.zubrilkaenglish.databinding.FolderViewBinding
import com.example.zubrilkaenglish.utils.MYBUNDLE

class DirectoryWordsAdapter(navController: NavController): RecyclerView.Adapter<DirectoryWordsAdapter.DirectoryWordsHolder>() {

    private var listFolders = emptyList<String>()

    //navController нужен для перехода на другой фрагмент при нажатии на элемент списка
    private var navController = navController


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DirectoryWordsHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.folder_view,parent,false)
        return DirectoryWordsHolder(view, navController)
    }

    override fun onBindViewHolder(holder: DirectoryWordsHolder, position: Int) {
        holder.bind(listFolders[position])
    }

    override fun getItemCount(): Int {
        return listFolders.size
    }

    public fun setList(listNamesFolders: List<String>){
        this.listFolders = listNamesFolders
        notifyDataSetChanged()
    }

    class DirectoryWordsHolder(view: View, navController: NavController): RecyclerView.ViewHolder(view){
        val binding = FolderViewBinding.bind(view)

        init {
            view.setOnClickListener {
                Log.d("ZeLog","нажат элемент "+adapterPosition)
                MYBUNDLE["number_position_into_list"] = adapterPosition
                navController.navigate(R.id.action_directoryWordsFragment_to_listWordsFragment)
            }
        }

        fun bind(nameFolder: String){
            binding.nameFolder.text = nameFolder
        }
    }
}