package com.example.ze_adminandroid.screens.editWord

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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