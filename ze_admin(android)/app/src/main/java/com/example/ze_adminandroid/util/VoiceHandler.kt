package com.example.ze_adminandroid.util

import android.media.MediaPlayer
import java.io.File

/**
 * класс занимается воспроизведением аудио
 */
class VoiceHandler {


    fun play(/*voice: Voice?*/file: File) {

        if (!file.isDirectory&&file.name.endsWith(".mp3")){
            playMp3File(file)
        }

        /*voice?.voiceData?.let { playMp3ByteArray(it) }*/
    }





    private fun playMp3File(/*mp3ByteArray: ByteArray*/file: File) {

//        val tempMp3 = File.createTempFile("temp", "mp3") // Создаем временный файл
//        tempMp3.deleteOnExit() // Удаляем временный файл после воспроизведения

//        val fos = FileOutputStream(tempMp3)
//        fos.write(mp3ByteArray)
//        fos.close()

        val mediaPlayer = MediaPlayer()


        try {
            mediaPlayer.setDataSource(file.absolutePath)
            mediaPlayer.prepare()
            mediaPlayer.start()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}