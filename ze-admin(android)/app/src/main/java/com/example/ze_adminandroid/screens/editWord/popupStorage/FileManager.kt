package com.example.ze_adminandroid.screens.editWord.popupStorage

import android.os.Environment
import java.io.File

class FileManager() {

    var downloadFolder: File
    init {
        downloadFolder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
    }

    /**
     * функция выдаст список файлов находящихся в директории
     */
    fun getListFiles(file: File): Array<File>? {
            return file.listFiles()
    }
}