package com.example.ze_adminandroid.screens.editWord

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.ze_adminandroid.R
import com.example.ze_adminandroid.databinding.FragmentEditWordBinding
import com.example.ze_adminandroid.models.Word
import com.example.ze_adminandroid.util.myBundle

/**
 * в фрагменте редактируется или создается новый Word
 */
class EditWordFragment : Fragment() {

    private lateinit var viewModel: EditWordViewModel
    private lateinit var binding: FragmentEditWordBinding
    private lateinit var editedWord: Word

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
        editedWord = myBundle["editedWord"] as Word

        showWordFields()
        setLiseners()
    }

    /**
     * установит слушатели на кнопки
     */
    private fun setLiseners() {
        binding.folderButton.setOnClickListener{
//            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT_TREE)
//            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
//            startActivity(intent)
            val permission = Manifest.permission.READ_EXTERNAL_STORAGE
            val requestCode = 1

            if (ContextCompat.checkSelfPermission(requireActivity(), permission) != PackageManager.PERMISSION_GRANTED) {
                println("no permission")
                ActivityCompat.requestPermissions(requireActivity(), arrayOf(permission), requestCode)
            }else {
                println("permission yes")

            findNavController().navigate(R.id.action_editWordFragment_to_filesFragment)
            }
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