package com.example.zubrilkaenglish.repositories.retrofit

import android.media.AudioFormat
import android.media.AudioManager
import android.media.AudioTrack
import android.media.MediaPlayer
import android.widget.Toast
import com.example.zubrilkaenglish.models.Word
import com.example.zubrilkaenglish.utils.MyApplication
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream

class RetrofitService {
    suspend fun getAllWords(): List<Word>? {
        try{
            return RetrofitInstance.wordApi.getAllWords().body()//TODO там приходит response его нужно обработать здесь
        }catch (e:Exception){
            e.printStackTrace()
            Toast.makeText(MyApplication.context,"ошибка подключения к серверу",Toast.LENGTH_LONG).show()
            return null
        }
    }

    suspend fun getDictionaryVersion(): String? {
        try{
            return RetrofitInstance.propApi.getDictionaryVersion().body()?.value
        }catch (e:Exception){
            e.printStackTrace()
            Toast.makeText(MyApplication.context,"ошибка подключения к серверу",Toast.LENGTH_LONG).show()
            return null
        }
    }

    fun getVoiceByName(){
        val name = "description.mp3"
        GlobalScope.launch(Dispatchers.Default) {
            val response/*: Response<ResponseBody>*/ = RetrofitInstance.voiceApi.getVoiceByName(name)

            val bytes: ByteArray? = response.body()?.bytes()
            if (bytes != null) {
                println(bytes.size)
                println(bytes.decodeToString())

                playMp3ByteArray(bytes)
            }else{
                println("bytes.size == null")
            }

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

        // Дополнительные операции с mediaPlayer, например, обработка окончания воспроизведения
        // mediaPlayer.setOnCompletionListener { ... }
    }
}
























