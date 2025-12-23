package com.example.weatherapp.feature.auth.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.feature.auth.domain.repository.AuthRepository
import com.example.weatherapp.feature.auth.domain.validation.Validator
import com.example.weatherapp.feature.auth.domain.validation.ValidationResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel(
    private val authRepository: AuthRepository,
    private val validator: Validator
) : ViewModel() {

    private val _loginState = MutableStateFlow<LoginState>(LoginState.Idle)
    val loginState: StateFlow<LoginState> = _loginState

    private val _registrationState = MutableStateFlow<RegistrationState>(RegistrationState.Idle)
    val registrationState: StateFlow<RegistrationState> = _registrationState

    fun login(email: String, password: String) {
        when (val emailValidation = validator.validateEmail(email)) {
            is ValidationResult.Error -> {
                _loginState.value = LoginState.Error(emailValidation.message)
                return
            }
            else -> {}
        }

        when (val passwordValidation = validator.validatePassword(password)) {
            is ValidationResult.Error -> {
                _loginState.value = LoginState.Error(passwordValidation.message)
                return
            }
            else -> {}
        }

        _loginState.value = LoginState.Loading
        viewModelScope.launch {
            try {
                val success = authRepository.login(email, password)
                _loginState.value = if (success) {
                    LoginState.Success
                } else {
                    LoginState.Error(validator.getInvalidCredentialsError())
                }
            } catch (e: Exception) {
                _loginState.value = LoginState.Error(validator.getLoginError(e.message))
            }
        }
    }

    fun register(email: String, password: String, confirmPassword: String) {
        when (val emailValidation = validator.validateEmail(email)) {
            is ValidationResult.Error -> {
                _registrationState.value = RegistrationState.Error(emailValidation.message)
                return
            }
            else -> {}
        }

        when (val passwordValidation = validator.validatePassword(password)) {
            is ValidationResult.Error -> {
                _registrationState.value = RegistrationState.Error(passwordValidation.message)
                return
            }
            else -> {}
        }

        when (val confirmPasswordValidation = validator.validateConfirmPassword(password, confirmPassword)) {
            is ValidationResult.Error -> {
                _registrationState.value = RegistrationState.Error(confirmPasswordValidation.message)
                return
            }
            else -> {}
        }

        _registrationState.value = RegistrationState.Loading
        viewModelScope.launch {
            try {
                val success = authRepository.register(email, password)
                _registrationState.value = if (success) {
                    RegistrationState.Success
                } else {
                    RegistrationState.Error(validator.getUserAlreadyExistsError())
                }
            } catch (e: Exception) {
                _registrationState.value = RegistrationState.Error(validator.getRegistrationError(e.message))
            }
        }
    }

    fun clearLoginState() {
        _loginState.value = LoginState.Idle
    }

    fun clearRegisterState() {
        _registrationState.value = RegistrationState.Idle
    }
}
