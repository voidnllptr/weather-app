package com.example.weatherapp.feature.auth.presentation.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.weatherapp.databinding.FragmentLoginBinding
import com.example.weatherapp.feature.auth.presentation.viewmodel.AuthViewModel
import com.example.weatherapp.feature.auth.presentation.viewmodel.LoginState
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import com.google.android.material.snackbar.Snackbar
import org.koin.androidx.viewmodel.ext.android.viewModel  // ← KOIN ИМПОРТ

class LogInFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AuthViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupClickListeners()
        observeViewModel()
    }

    private fun setupClickListeners() {
        binding.btnLogin.setOnClickListener {
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()
            viewModel.login(email, password)
        }

        binding.tvGoToRegister.setOnClickListener {
            val action = LogInFragmentDirections.actionLoginToRegistration()
            findNavController().navigate(action)
        }
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.loginState.collectLatest { state ->
                when (state) {
                    LoginState.Idle -> {
                    }
                    LoginState.Loading -> {
                        showLoading()
                    }
                    LoginState.Success -> {
                        val action = LogInFragmentDirections.actionLoginToWeather()
                        findNavController().navigate(action)
                    }
                    is LoginState.Error -> {
                        showError(state.message)
                        viewModel.clearLoginState()
                    }
                }
            }
        }
    }

    private fun showLoading() {
        binding.btnLogin.isEnabled = false
    }

    private fun showError(message: String) {
        binding.btnLogin.isEnabled = true
        Snackbar.make(binding.root, message, Snackbar.LENGTH_LONG).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
