package com.example.zubrilkaenglish.services

import android.media.MediaPlayer
import com.example.zubrilkaenglish.models.Voice
import java.io.File
import java.io.FileOutputStream

/**
 * класс занимается воспроизведением аудио
 */
class VoiceHandler private constructor(){
companion object{
    val instanse: VoiceHandler by lazy { VoiceHandler() }
}

    fun play(voice: Voice?) {
        voice?.voiceData?.let { playMp3ByteArray(it) }
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
                mediaPlayer.setOnCompletionListener {
                    mediaPlayer.release() // Освобождаем ресурсы MediaPlayer после завершения воспроизведения
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
    }
}