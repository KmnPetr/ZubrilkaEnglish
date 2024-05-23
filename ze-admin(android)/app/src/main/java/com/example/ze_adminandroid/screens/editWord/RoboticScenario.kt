package com.example.ze_adminandroid.screens.editWord

import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import com.example.ze_adminandroid.models.Voice
import com.example.ze_adminandroid.screens.editWord.popupStorage.FileManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.io.File


/**
 * класс отвечает за програмируемый сценарий действий взамен ручного
 * например запуск определенных методов
 * клики по определенным областям экрана
 * для автоматизированной загрузки аудио с интернета и страниц
 */
class RoboticScenario private constructor(){
    companion object{
        val instance: RoboticScenario by lazy { RoboticScenario() }
    }

    private val fileManager = FileManager.instanse
    private var listActions: MutableList<() -> Unit> = mutableListOf()
    //переменная для отслеживания возвращения фрагмента EditWord к жизни
    private var isFragmentOnResume:Boolean = false
    //укажет что сценарий уже запущен потомучто навконтроллер при возвращении назад по стеку запускает его второй раз
    private var isRun = false

    /**
     * функция запустит последовательность действий для получения voice с вэбсайта myefe
     */
    fun myefeWebsiteGetVoice(fragment: EditWordFragment, createVoice: (Voice) -> Unit) {
        if (!isRun){
            isRun = true
            listActions.clear()

            //запуск кнопки перехода в браузер
        listActions.add{clickImageButton(fragment.getBinding().internetButton)}
            //запуск кнопки перехода в webView
//            listActions.add{clickButton(fragment.getBinding().buttonWebView)}
            //подождем когда фрагмент вернется в активное состояниее
            listActions.add { waitFragmentOnResume() }
            //достанем файл voice из репозитория
            listActions.add { getFileMp3(fragment,createVoice) }
            //дадим название нового файла voice
            listActions.add { clickButton(fragment.getBinding().copyLinkVoiceButton) }
            //пожалуй сохраним файл
            listActions.add { clickButton(fragment.getBinding().saveButton) }

            GlobalScope.launch {
                runActions()
            }
        }
    }

    /**
     * сэметирует нажатие на Button
     */
    private fun clickButton(button: Button) {
        val job = GlobalScope.launch {
            withContext(Dispatchers.Main){
                button.performClick()
            }
        }
        runBlocking { job.join() }
    }
    /**
     * сэметирует нажатие на ImageButton
     */
    private fun clickImageButton(button: ImageButton) {
        val job = GlobalScope.launch {
            withContext(Dispatchers.Main){
                button.performClick()
            }
        }
        runBlocking { job.join() }
    }
    /**
     * вытащит файл mp3 из dowload папки и установит его
     */
    private fun getFileMp3(fragment: EditWordFragment, createVoice: (Voice) -> Unit) {
        val job = GlobalScope.launch {
            var file:File? = null
            var countFiles = 0
            fileManager.listDownloadedFiles().forEach {
                if (it.name.endsWith(".mp3")) {
                    countFiles++
                    if (countFiles==1){
                        file = it
                    } else {
                        file = null
                        Toast.makeText(fragment.requireActivity(),"Проверь количество файлов \"mp3\"",Toast.LENGTH_SHORT).show()
                    }
                }
            }
            if (file!=null){
                withContext(Dispatchers.Main){
                    val voice = Voice(fileManager.getFileName(file!!),fileManager.getByteArray(file!!))
                    createVoice(voice)
                    //необходимо для дальнейшего удаления файла
                    fileManager.usedFile = file
                }
            }
        }
        runBlocking { job.join() }
    }


    /**
     * сделает паузу пока активити неактивно
     * подождет пока вызовется метод onResume у фрагмента
     */
    private fun waitFragmentOnResume() {
        val job = GlobalScope.launch {
            //вот они такие костыли
            delay(1500)
            while (!isFragmentOnResume) {
                delay(50)
            }
        }
        runBlocking { job.join()
        }
    }




    /**
     * запустит выполнение действий из листа
     */
    private fun runActions(){
        listActions.forEach { it.invoke() }
        isRun = false
    }

    /**
     * даст сигнал к продолжению сценария после возвращения актитвити в активное состояние
     */
    fun onFragmentResume() {
        isFragmentOnResume = true
        GlobalScope.launch {
            delay(1000)
            //дадим сценарию время чтобы понять что можно продолжить и вернем переменную назад
            isFragmentOnResume = false
        }
    }
}