package com.example.ze_adminandroid.screens.editWord.popupStorage

import android.os.Environment
import java.io.File
import java.io.FileInputStream

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

    fun getFileName(file: File): String {
        return file.name
    }

    /**
     * метод вернет массив байтов файла
     */
    fun getByteArray(file: File): ByteArray {
            val fis = FileInputStream(file)
            val byteArray: ByteArray = fis.readBytes()
            fis.close()
            println("size of file: " + byteArray.size)
            return byteArray
    }
}