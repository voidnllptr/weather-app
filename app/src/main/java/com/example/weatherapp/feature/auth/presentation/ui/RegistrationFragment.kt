package com.example.weatherapp.feature.auth.presentation.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.weatherapp.databinding.FragmentRegistrationBinding
import com.example.weatherapp.feature.auth.presentation.viewmodel.AuthViewModel
import com.example.weatherapp.feature.auth.presentation.viewmodel.RegistrationState
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import com.google.android.material.snackbar.Snackbar
import org.koin.androidx.viewmodel.ext.android.viewModel  // ← ДОБАВЬ

class RegistrationFragment : Fragment() {

    private var _binding: FragmentRegistrationBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AuthViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegistrationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupClickListeners()
        observeViewModel()
    }

    private fun setupClickListeners() {
        binding.btnRegister.setOnClickListener {
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()
            val confirmPassword = binding.etConfirmPassword.text.toString().trim()
            viewModel.register(email, password, confirmPassword)
        }

        binding.tvGoToLogin.setOnClickListener {
            val action = RegistrationFragmentDirections.actionRegistrationToLogin()
            findNavController().navigate(action)
        }
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.registrationState.collectLatest { state ->
                when (state) {
                    RegistrationState.Idle -> {}
                    RegistrationState.Loading -> showLoading()
                    RegistrationState.Success -> {
                        val action = RegistrationFragmentDirections.actionRegistrationToWeather()
                        findNavController().navigate(action)
                    }
                    is RegistrationState.Error -> {
                        showError(state.message)
                        viewModel.clearRegisterState()
                    }
                }
            }
        }
    }

    private fun showLoading() {
        binding.btnRegister.isEnabled = false
    }

    private fun showError(message: String) {
        binding.btnRegister.isEnabled = true
        Snackbar.make(binding.root, message, Snackbar.LENGTH_LONG).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
