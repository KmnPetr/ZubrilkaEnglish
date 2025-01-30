package com.example.ze_adminandroid.screens.editWord.popupStorage

import android.Manifest
import android.content.pm.PackageManager
import android.os.Environment
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import com.example.ze_adminandroid.utils.TEG
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException

class FileManager private constructor(){
    companion object{
        val instanse: FileManager by lazy { FileManager() }
    }

    var downloadFolder: File
    //будет хранить последний использованный файл, чтобы при необходимости удалить его
    var usedFile: File? = null
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

    /**
     * выдаст список файлов в папке download
     */
    fun listDownloadedFiles(): List<File> {
        return downloadFolder.listFiles()?.toList() ?: listOf()
    }


    /**
     * попросит разрешение на доступ к хранилищу
     */
    fun getPermission(activity: FragmentActivity) {
        Log.d(TEG,"METHOD: getPermission")

        val permissionWrite = Manifest.permission.WRITE_EXTERNAL_STORAGE
        val requestCode = 1

        if (ContextCompat.checkSelfPermission(activity, permissionWrite) != PackageManager.PERMISSION_GRANTED) {
            Log.d(TEG,"METHOD: permissionWrite wasn't granted")
            ActivityCompat.requestPermissions(activity, arrayOf(permissionWrite), requestCode)
        }
    }

    /**
     * удалит использованный файл
     */
    fun deleteUsedFile(){
        println("deleteUsedFile() file = $usedFile")
        usedFile?.delete()
        usedFile = null
    }

    /**
     * сохранит новый файл в папке download
     */
    fun saveNewFile(fileName: String?, bytes: ByteArray?) {

        println(downloadFolder.absolutePath)

        val file = File(downloadFolder.absolutePath+"/"+fileName)
        try{
            val fos = FileOutputStream(file)
            fos.write(bytes)
            fos.close()
        } catch (e: IOException) {
            throw RuntimeException(e);
        }
    }
}