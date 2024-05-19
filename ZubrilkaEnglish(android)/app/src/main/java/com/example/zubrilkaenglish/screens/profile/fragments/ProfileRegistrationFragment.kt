package com.example.zubrilkaenglish.screens.profile.fragments

import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.zubrilkaenglish.R
import com.example.zubrilkaenglish.databinding.FragmentProfileRegistrationBinding
import com.example.zubrilkaenglish.models.Profile
import com.example.zubrilkaenglish.repositories.ProfileRepository
import com.example.zubrilkaenglish.screens.profile.ProfileViewModel

/**
 * в этом фрагменте будет происходить регистрация пользователя
 */
class ProfileRegistrationFragment : Fragment() {
    private lateinit var binding: FragmentProfileRegistrationBinding
    private lateinit var viewModel: ProfileViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileRegistrationBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)

        binding.username.setText(viewModel.regName)
        binding.email.setText(viewModel.regEmail)
        binding.password.setText(viewModel.regPassword.value )
        binding.repeatPassword.setText(viewModel.regSecondPassword.value)

        setListeners()
    }

    /**
     * регистрация слушателей на различные кнопки и ссылки
     */
    private fun setListeners() {
        viewModel.profile.observe(viewLifecycleOwner){profileExists(it)}
        binding.enterLink.setOnClickListener { enterLinkClick(it) }
        binding.buttonCreateProfile.setOnClickListener { requestCreateProfile() }
        viewModel.listValidationErrors.observe(viewLifecycleOwner){ showErrors(it) }
        binding.username.addTextChangedListener(setupName())
        binding.email.addTextChangedListener(setupEmail())
        binding.password.addTextChangedListener(setupPassword())
        binding.repeatPassword.addTextChangedListener(setupRepeatPassword())
        viewModel.regPassword.observe(viewLifecycleOwner){comparePassword()}
        viewModel.regSecondPassword.observe(viewLifecycleOwner){comparePassword()}
    }

    /**
     * переведет на другой фрагмент с информацией о профиле если он найдется в БД
     */
    private fun profileExists(profile: Profile?) {
        if (profile!=null){
            findNavController().popBackStack()
        }
    }

    //передаст значение Password с формы в view model
    private fun setupRepeatPassword(): TextWatcher {
        return object: TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {viewModel.regSecondPassword.value = s.toString()}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {viewModel.regSecondPassword.value = s.toString()}
            override fun afterTextChanged(s: Editable?) {viewModel.regSecondPassword.value = s.toString()}
        }
    }
    //сравнит первый и второй вариант пароля
    private fun comparePassword() {
        if ( viewModel.regPassword.value == viewModel.regSecondPassword.value){
            binding.repeatPassword.setTextColor(Color.parseColor("#7FBD00"))
            viewModel.isPasswordsMatch = true
        }else {
            binding.repeatPassword.setTextColor(Color.BLACK)
            viewModel.isPasswordsMatch = false
        }
    }

    //передаст значение Password с формы в view model
    private fun setupPassword(): TextWatcher {
        return object: TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {viewModel.regPassword.value = s.toString()}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {viewModel.regPassword.value = s.toString()}
            override fun afterTextChanged(s: Editable?) {viewModel.regPassword.value = s.toString()}
        }
    }

    //передаст значение Email с формы в view model
    private fun setupEmail(): TextWatcher {
        return object: TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {viewModel.regEmail = s.toString()}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {viewModel.regEmail = s.toString()}
            override fun afterTextChanged(s: Editable?) {viewModel.regEmail = s.toString()}
        }
    }

    //передаст значение name с формы в view model
    private fun setupName(): TextWatcher {
        return object: TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {viewModel.regName = s.toString()}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {viewModel.regName = s.toString()}
            override fun afterTextChanged(s: Editable?) {viewModel.regName = s.toString()}
        }
    }


    /**
     * покажет ошибки при заполнении полей
     */
    private fun showErrors(it: List<String>?) {
        if (it != null){
            binding.errorField.visibility = View.VISIBLE
        } else binding.errorField.visibility = View.GONE

        binding.errorText.text=""
        it?.forEach {
            binding.errorText.append(it+"\n")
        }
    }

    /**
     * сформирует запрос для отправки на сервер на регистрацию
     */
    private fun requestCreateProfile() {
        if (!viewModel.isPasswordsMatch) {
            ProfileRepository.instance.passwordMismatchError()
            return
        }
        val newProfile = Profile(
            -1,
            viewModel.regEmail,
            viewModel.regPassword.value!!,
            viewModel.regName,
            null,
            null,
            null
        )
        ProfileRepository.instance.registrationRequest(newProfile)
    }

    /**
     * обработает нажатие на ссылку перехода на страницу входа
     */
    private fun enterLinkClick(it: View?) {
        findNavController().navigate(R.id.action_profileRegistrationFragment_to_profileEnterFragment)
    }
}