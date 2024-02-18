package com.example.zubrilkaenglish.utils

import android.media.MediaPlayer
import com.example.zubrilkaenglish.models.Voice
import javazoom.jl.decoder.Bitstream
import javazoom.jl.decoder.Decoder
import javazoom.jl.decoder.Header
import javazoom.jl.decoder.SampleBuffer
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

/**
 * класс занимается перекодированием и воспроизведением аудио
 */
class VoiceHandler {



    fun play(voice: Voice) {
        voice.voiceData?.let { playMp3ByteArray(it) }


        decodeMP3ToPCM(voice.voiceData)
    }


    fun decodeMP3ToPCM(mp3ByteArray: ByteArray?): ByteArray {
        println("НАЧАЛЬНЫЙ РАЗМЕР: "+mp3ByteArray?.size)
        val bitstream = Bitstream(ByteArrayInputStream(mp3ByteArray))
        val decoder = Decoder()

        val outputData: ByteArrayOutputStream = ByteArrayOutputStream()

        try {
            var output: ByteArray
            var pcmBuffer: SampleBuffer

            var header: Header? = bitstream.readFrame()
            while (header != null) {
                pcmBuffer = decoder.decodeFrame(header, bitstream) as SampleBuffer

                val fff: ShortArray? = pcmBuffer.buffer
                output = pcmBuffer.buffer.map { it.toByte() }.toByteArray()
//                output = pcmBuffer.buffer
                outputData.write(output, 0, output.size)

                bitstream.closeFrame()
                header = bitstream.readFrame()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        println("КОНЕЧНЫЙ РАЗМЕР: "+outputData.toByteArray().size)
        return outputData.toByteArray()
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

        // Дополнительные операции с mediaPlayer, например, обработка окончания воспроизведения
        // mediaPlayer.setOnCompletionListener { ... }
    }



}