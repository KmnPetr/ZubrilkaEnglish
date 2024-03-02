package com.example.ze_adminandroid.screens.files

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ze_adminandroid.R
import com.example.ze_adminandroid.databinding.FileViewBinding
import java.io.File

class FilesAdapter: RecyclerView.Adapter<FilesAdapter.FileHolder>() {

    private var listFiles= emptyList<File>()

    class FileHolder(view: View): RecyclerView.ViewHolder(view){
        private val binding: FileViewBinding = FileViewBinding.bind(view)
        fun bind(file: File) {
            binding.filePath.setText(file.name)

            binding.root.setOnClickListener {

            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FileHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.file_view,parent,false)
        return FileHolder(view)
    }

    override fun getItemCount(): Int = listFiles.size

    override fun onBindViewHolder(holder: FileHolder, position: Int) {
        holder.bind(listFiles[position])
    }

    fun setList(list: List<File>){
        this.listFiles = list
        notifyDataSetChanged()
    }
}