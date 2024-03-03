package com.example.ze_adminandroid.screens.files

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ze_adminandroid.R
import com.example.ze_adminandroid.databinding.FileViewBinding
import java.io.File

class FilesAdapter(
    private val onClickFile: (File) -> Unit,
    private val onClickButtonPlay: (File) -> Unit
) : RecyclerView.Adapter<FilesAdapter.FileHolder>() {

    var listFiles= emptyList<File>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FileHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.file_view,parent,false)
        return FileHolder(view)
    }

    override fun getItemCount(): Int = listFiles.size

    override fun onBindViewHolder(holder: FileHolder, position: Int) {
        holder.bind(listFiles[position], onClickFile, onClickButtonPlay)
    }

    fun setList(list: List<File>){
        this.listFiles = list
        println("setList: size: "+list.size)
        notifyDataSetChanged()
    }


    class FileHolder(view: View): RecyclerView.ViewHolder(view){
        private val binding: FileViewBinding = FileViewBinding.bind(view)
        fun bind(
            file: File,
            onClickFile: (File) -> Unit,
            onClickButtonPlay: (File) -> Unit
        ) {
            binding.filePath.setText(file.name)

            binding.root.setOnClickListener {
                onClickFile(file)
            }

            setVisibleImage(file)
            settingButtonPlay(file,onClickButtonPlay)
        }
        private fun settingButtonPlay(file: File, onClickButtonPlay: (File) -> Unit) {
            if (file.name.endsWith(".mp3")){
                binding.buttonPlay.isEnabled = true
                binding.buttonPlay.visibility = View.VISIBLE
                binding.buttonPlay.setOnClickListener {
                    onClickButtonPlay(file)
                }
            }else{
                binding.buttonPlay.isEnabled = false
                binding.buttonPlay.visibility = View.GONE
            }
        }

        private fun setVisibleImage(file: File) {
            if (file.isDirectory){
                binding.imageFolder.visibility = View.VISIBLE
                binding.imageMp3.visibility = View.GONE
                binding.imageUnknownFile.visibility = View.GONE
            }else if(file.name.endsWith(".mp3")){
                binding.imageFolder.visibility = View.GONE
                binding.imageMp3.visibility = View.VISIBLE
                binding.imageUnknownFile.visibility = View.GONE
            } else if (file.isFile){
                binding.imageFolder.visibility = View.GONE
                binding.imageMp3.visibility = View.GONE
                binding.imageUnknownFile.visibility = View.VISIBLE
            }
        }
    }
}