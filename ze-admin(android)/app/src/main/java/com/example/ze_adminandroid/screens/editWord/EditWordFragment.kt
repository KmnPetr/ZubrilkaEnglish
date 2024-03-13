package com.example.ze_adminandroid.screens.editWord

import android.Manifest
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.ze_adminandroid.databinding.FragmentEditWordBinding
import com.example.ze_adminandroid.models.Voice
import com.example.ze_adminandroid.models.Word
import com.example.ze_adminandroid.repositories.VoiceRepository
import com.example.ze_adminandroid.repositories.WordRepository
import com.example.ze_adminandroid.screens.editWord.popupStorage.PopUpStorage
import com.example.ze_adminandroid.screens.editWord.popupTopics.PopUpTopics
import com.example.ze_adminandroid.utils.myBundle
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.EventBus

/**
 * в фрагменте редактируется или создается новый Word
 */
class EditWordFragment : Fragment() {

    private lateinit var viewModel: EditWordViewModel
    private lateinit var binding: FragmentEditWordBinding
    private lateinit var editedWord: Word
    private lateinit var wordRepository: WordRepository
    private lateinit var voiceRepository: VoiceRepository
    // новый Voiceопределяем здесь чтобы после выхода из фрагмента очищался
    var createdVoice: MutableLiveData<Voice> = MutableLiveData()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEditWordBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(EditWordViewModel::class.java)
        wordRepository = WordRepository.instance
        voiceRepository = VoiceRepository.instance
        editedWord = myBundle["editedWord"] as Word

        showWordFields()
        setLiseners()
        onChangeVoice()
    }

    /**
     * производит разные изменения при перевыборе Voice
     */
    private fun onChangeVoice() {
        createdVoice.observe(viewLifecycleOwner){
            if (it?.voiceData != null){
                binding.fileSize.setText("fileSize = " + it.voiceData?.size)
            }
        }
    }

    /**
     * установит слушатели на кнопки
     */
    private fun setLiseners() {
        binding.folderButton.setOnClickListener{
            getPermission()
            val popup = PopUpStorage(requireContext(),::createVoice)
            popup.show()
        }
        binding.internetButton.setOnClickListener {
            clipText(editedWord.foreignWord)
            temporarySavingWord() //временно сохраним недоделанное слово в БД
            openBrowser()
        }
        binding.selectTopicButton.setOnClickListener {
            PopUpTopics(requireContext(),viewModel.namesTopics,::setTopic).show()
        }
        binding.copyLinkVoiceButton.setOnClickListener {
            setLinkVoice()
        }
        binding.saveButton.setOnClickListener {
            if(isWordReady()){
                saveWord()
                saveVoice()
                Toast.makeText(requireContext(),"successfully!!",Toast.LENGTH_SHORT).show()
                GlobalScope.launch(Dispatchers.Default) {
                    delay(1500) // ожидание
                    launch(Dispatchers.Main) {
                        findNavController().popBackStack()
                    }
                }
            }
        }
    }

    /**
     * временно сохраним недоделанное слово в БД
     */
    private fun temporarySavingWord() {
        val word = Word(
            editedWord.localBaseId,
            editedWord.id,
            binding.foreignWord.text.toString(),
            binding.transcription.text.toString(),
            binding.translation.text.toString(),
            binding.description.text.toString(),
            binding.topic.text.toString(),
            binding.linkVoice.text.toString(),
            binding.linkImage.text.toString(),
            editedWord.sorting_value,
            System.currentTimeMillis(),
            false //важно, оно не доделано
        )
        wordRepository.saveEditableWord(word)
    }

    /**
     * проверит перед сохранением все поля на наличие ошибок и прочего
     */
    private fun isWordReady(): Boolean {
        return if (binding.foreignWord.text.toString().isEmpty()){
            Toast.makeText(requireContext(),"foreignWord is empty!",Toast.LENGTH_SHORT).show()
            false
        }else if(binding.translation.text.toString().isEmpty()){
            Toast.makeText(requireContext(),"translation is empty!",Toast.LENGTH_SHORT).show()
            false
        }else if(binding.linkVoice.text.toString().isEmpty()){
            Toast.makeText(requireContext(),"linkVoice is empty!",Toast.LENGTH_SHORT).show()
            false
        }else if(createdVoice.value?.voiceData ==null){
            Toast.makeText(requireContext(),"voiceData is empty!",Toast.LENGTH_SHORT).show()
            false
        } else true
    }

    /**
     * установит значение в поле linkVoice
     */
    private fun setLinkVoice() {
        val str:String = binding.foreignWord.text.toString()
        val prefix:String = binding.prefix.text.toString()

        binding.linkVoice.setText(str+prefix+".mp3")
    }

    /**
     * сохранит новый Voice в БД
     */
    private fun saveVoice() {
        if (createdVoice.value!=null&& createdVoice.value!!.voiceData!=null){
            val voice = Voice(binding.linkVoice.text.toString(),
                createdVoice.value?.voiceData
            )
            voiceRepository.saveNewVoice(voice)
            println("сохранено Voice")
            println("name: "+ voice.voiceName)
            println("data size: "+ (voice.voiceData?.size ?: "null"))
        }
    }

    /**
     * сохранит измененное слово в БД
     */
    private fun saveWord() {
        val word = Word(
            editedWord.localBaseId,
            editedWord.id,
            binding.foreignWord.text.toString(),
            binding.transcription.text.toString(),
            binding.translation.text.toString(),
            binding.description.text.toString(),
            binding.topic.text.toString(),
            binding.linkVoice.text.toString(),
            binding.linkImage.text.toString(),
            editedWord.sorting_value,
            null,
            true
        )
        wordRepository.saveEditableWord(word)
    }

    /**
     * откроет страницу в браузере
     */
    private fun openBrowser() {
        val url = "https://myefe.ru/anglijskaya-transkriptsiya.html"
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(intent)
    }

    /**
     * скопирует текст в буфер обмена
     */
    private fun clipText(foreignWord: String?) {
        val clipboardManager = requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("Text", foreignWord)
        clipboardManager.setPrimaryClip(clip)
        // Выводим уведомление об успешном копировании
        Toast.makeText(requireActivity(), "Скопировано в буфер обмена: " + foreignWord, Toast.LENGTH_SHORT).show()

    }

    /**
     * попросит разрешение на доступ к хранилищу
     */
    private fun getPermission() {
        val permission = Manifest.permission.READ_EXTERNAL_STORAGE
        val requestCode = 1

        if (ContextCompat.checkSelfPermission(requireActivity(), permission) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(permission), requestCode)
        }
    }

    /**
     * заполнит поля фрагмента
     */
    private fun showWordFields() {
        binding.id.setText("id = " + editedWord.id)
        binding.foreignWord.setText(editedWord.foreignWord)
        binding.transcription.setText(editedWord.transcription)
        binding.translation.setText(editedWord.translation)
        binding.description.setText(editedWord.description)
        binding.topic.setText(editedWord.topic)
        binding.linkVoice.setText(editedWord.link_voice)
        binding.linkImage.setText(editedWord.link_image)
        binding.sortingValue.setText("sortingValue = " + editedWord.sorting_value)
    }

    /**
     * установит значение в поле топик
     */
    private fun setTopic(topicName: String){
        binding.topic.setText(topicName)
    }

    /**
     * инициализирует переменную createdVoice
     */
    private fun createVoice(createdVoice: Voice){
        this.createdVoice.value = createdVoice
    }

}