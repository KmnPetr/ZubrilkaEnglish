package com.example.zubrilkaenglish.screens.myCards

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.example.zubrilkaenglish.R
import com.example.zubrilkaenglish.databinding.FolderViewBinding
import com.example.zubrilkaenglish.utils.MYBUNDLE

class MyCardsAdapter(navController: NavController): RecyclerView.Adapter<MyCardsAdapter.MyCardsHolder>() {

    private var listFolders = emptyList<String>()

    //navController нужен для перехода на другой фрагмент при нажатии на элемент списка
    private var navController = navController


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyCardsHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.folder_view,parent,false)
        return MyCardsHolder(view, navController)
    }

    override fun onBindViewHolder(holder: MyCardsHolder, position: Int) {
        holder.bind(listFolders[position])
    }

    override fun getItemCount(): Int {
        return listFolders.size
    }

    fun setList(listNamesFolders: List<String>){
        this.listFolders = listNamesFolders
        notifyDataSetChanged()
    }

    class MyCardsHolder(view: View, navController: NavController): RecyclerView.ViewHolder(view){
        val binding = FolderViewBinding.bind(view)

        init {
            view.setOnClickListener {
                MYBUNDLE["number_position_into_list"] = adapterPosition
                navController.navigate(R.id.action_myCardsFragment_to_listCardsFragment)
            }
        }

        fun bind(nameFolder: String){
            binding.nameFolder.text = nameFolder
        }
    }
}