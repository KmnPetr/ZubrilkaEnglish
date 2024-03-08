package com.example.ze_adminandroid.screens.editWord

import android.app.Dialog
import android.content.Context
import android.view.Window
import com.example.ze_adminandroid.databinding.PopUpStorageBinding
import com.example.ze_adminandroid.util.VoiceHandler
import java.io.File

/**
 * класс покажет попап окошко с папками и файлами начиная от папки download
 */
class PopUpStorage(context: Context): Dialog(context) {
    val binding: PopUpStorageBinding
    val adapter: FilesAdapter
    val fileManager: FileManager
    val voiceHandler: VoiceHandler

    init {
        binding = PopUpStorageBinding.inflate(layoutInflater)
        adapter = FilesAdapter(::onClickFile,::onClickButtonPlay)
        fileManager = FileManager()
        voiceHandler = VoiceHandler()

        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setCancelable(true)
        setContentView(binding.root)
//        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val displayMetrics = context.resources.displayMetrics
        binding.root.layoutParams.width = (displayMetrics.widthPixels*0.66).toInt()
        binding.root.layoutParams.height = (displayMetrics.heightPixels * 0.66).toInt()

        binding.recyclerView.adapter = adapter

        adapter.setList(listOf(fileManager.downloadFolder))
    }
    private fun onClickFile(file: File){
        if(file.isDirectory){
            fileManager.getListFiles(file)?.let { adapter.setList(it.toList()) }
        }
    }
    private fun onClickButtonPlay(file: File){
        voiceHandler.play(file)
    }
}