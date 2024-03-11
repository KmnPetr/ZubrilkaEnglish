package com.example.ze_adminandroid.utils

import android.media.MediaPlayer
import com.example.ze_adminandroid.models.Voice
import java.io.File
import java.io.FileOutputStream

/**
 * класс занимается воспроизведением аудио
 */
class VoiceHandler {


    fun play(file: File) {
        if (!file.isDirectory&&file.name.endsWith(".mp3")){
            playMp3File(file)
        }
    }

    fun play(voice: Voice?) {
        voice?.voiceData?.let { playMp3ByteArray(it) }
    }




    private fun playMp3File(file: File) {
        val mediaPlayer = MediaPlayer()

        try {
            mediaPlayer.setDataSource(file.absolutePath)
            mediaPlayer.prepare()
            mediaPlayer.start()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun playMp3ByteArray(mp3ByteArray: ByteArray) {

        val tempMp3 = File.createTempFile("temp", "mp3") // Создаем временный файл
        tempMp3.deleteOnExit() // Удаляем временный файл после воспроизведения

        val fos = FileOutputStream(tempMp3)
        fos.write(mp3ByteArray)
        fos.close()

        val mediaPlayer = MediaPlayer()


        try {
            mediaPlayer.setDataSource(tempMp3.path) // Устанавливаем временный файл в качестве источника данных
            mediaPlayer.prepare()
            mediaPlayer.start()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}