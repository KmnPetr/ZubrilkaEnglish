package com.example.ze_adminandroid.screens.editWord.popupStorage

import android.app.Dialog
import android.content.Context
import android.view.Window
import com.example.ze_adminandroid.databinding.PopUpStorageBinding
import com.example.ze_adminandroid.models.Voice
import com.example.ze_adminandroid.screens.editWord.EditWordViewModel
import com.example.ze_adminandroid.utils.VoiceHandler
import java.io.File

/**
 * класс покажет попап окошко с папками и файлами начиная от папки download
 */
class PopUpStorage(
    context: Context,
    private val createVoice: (Voice) -> Unit
): Dialog(context) {
    val binding: PopUpStorageBinding
    val adapter: FilesAdapter
    val fileManager: FileManager
    val voiceHandler: VoiceHandler

    init {
        binding = PopUpStorageBinding.inflate(layoutInflater)
        adapter = FilesAdapter(::onClickFile,::onClickButtonPlay,::onClickButtonGetVoice)
        fileManager = FileManager.instanse
        voiceHandler = VoiceHandler()

        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setCancelable(true)
        setContentView(binding.root)

        val displayMetrics = context.resources.displayMetrics
        binding.root.layoutParams.width = (displayMetrics.widthPixels * 0.8).toInt()
        binding.root.layoutParams.height = (displayMetrics.heightPixels * 0.8).toInt()

        binding.recyclerView.adapter = adapter

        adapter.setList(fileManager.listDownloadedFiles())
    }
    private fun onClickFile(file: File){
        if(file.isDirectory){
            fileManager.getListFiles(file)?.let { adapter.setList(it.toList()) }
        }
    }
    private fun onClickButtonPlay(file: File){
        voiceHandler.play(file)
    }

    /**
     * возьмет содержимое файла и сделает из него voice
     * а также внесет файл в поле fileManager.usedFile для дальнейшего его удаления
     */
    private fun onClickButtonGetVoice(file: File){

        val voice = Voice(fileManager.getFileName(file),fileManager.getByteArray(file))
        createVoice(voice)
        fileManager.usedFile = file
        this.dismiss()
    }
}