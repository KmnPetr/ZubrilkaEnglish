package com.example.ze_adminandroid.screens.files

import android.os.Environment
import java.io.File

class FileManager() {
//    lateinit var rootFile: File
    init {val downloadFolder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
    val downloadFolderPath = downloadFolder.absolutePath

// Дальше можно выполнять операции с папкой Download, например, получать список файлов внутри
    val files = downloadFolder.listFiles()

// Вывести список файлов в консоль
    for (file in files) {
        println(file.name)
    }
    }
}