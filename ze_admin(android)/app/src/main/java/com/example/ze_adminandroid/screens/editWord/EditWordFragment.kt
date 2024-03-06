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
import androidx.lifecycle.ViewModelProvider
import com.example.ze_adminandroid.databinding.FragmentEditWordBinding
import com.example.ze_adminandroid.models.Word
import com.example.ze_adminandroid.repositories.WordRepository
import com.example.ze_adminandroid.util.myBundle

/**
 * в фрагменте редактируется или создается новый Word
 */
class EditWordFragment : Fragment() {

    private lateinit var viewModel: EditWordViewModel
    private lateinit var binding: FragmentEditWordBinding
    private lateinit var editedWord: Word
    private lateinit var wordRepository: WordRepository

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
        editedWord = myBundle["editedWord"] as Word

        showWordFields()
        setLiseners()
    }

    /**
     * установит слушатели на кнопки
     */
    private fun setLiseners() {
        binding.folderButton.setOnClickListener{
            getPermission()
            val popup = PopUpStorage(requireContext())
            popup.show()
        }
        binding.internetButton.setOnClickListener {
            clipText(editedWord.foreignWord)
            openBrowser()
        }
        binding.saveButton.setOnClickListener {
            val word = Word(
                null,
                editedWord.id,
                binding.foreignWord.text.toString(),
                binding.transcription.text.toString(),
                binding.translation.text.toString(),
                binding.description.text.toString(),
                binding.topic.text.toString(),
                binding.linkVoice.text.toString(),
                binding.linkImage.text.toString(),
                editedWord.sorting_value
            )
            wordRepository.saveEditableWord(word)
        }
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

}